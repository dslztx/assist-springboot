package me.dslztx.booter.javaconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

import me.dslztx.booter.client.activemq.noncluster.DESTTYPE;
import me.dslztx.booter.client.activemq.noncluster.MQConsumerManager;
import me.dslztx.booter.client.activemq.noncluster.MQNodesSync;
import me.dslztx.booter.client.activemq.noncluster.MQProducerManager;
import me.dslztx.booter.client.mysql.DataSourcePool;
import me.dslztx.booter.client.mysql.JdbcTemplatePool;
import me.dslztx.booter.property.ActiveMQProperties;
import me.dslztx.booter.property.MysqlProperties;

/**
 * @author dslztx
 */
@Configuration
public class JavaConfigSpot {

    /**
     * 注入对Spring容器的依赖
     */
    @Autowired
    ApplicationContext context;

    @Bean(name = "mqNodesSync")
    @Scope("prototype")
    public MQNodesSync defineMQNodesSync(CuratorFramework curatorFramework, ActiveMQProperties prop) {
        MQNodesSync mqNodesSync = new MQNodesSync(prop.getMqNodesPath());
        mqNodesSync.setCuratorFramework(curatorFramework);
        return mqNodesSync;
    }

    @Bean(name = "mqConsumerManager", initMethod = "init", destroyMethod = "close")
    // ActiveMQ客户端“not thread safe”，因此设置为“prototype”
    @Scope("prototype")
    public MQConsumerManager defineMQConsumerManager(MQNodesSync mqNodesSync, ActiveMQProperties prop) {
        MQConsumerManager mqConsumerManager =
            new MQConsumerManager(prop.getUsername(), prop.getPassword(), prop.getQueueForConsumer(), DESTTYPE.QUEUE);
        mqConsumerManager.setMqNodesSync(mqNodesSync);
        return mqConsumerManager;
    }

    @Bean(name = "mqProducerManager", initMethod = "init", destroyMethod = "close")
    // ActiveMQ客户端“not thread safe”，因此设置为“prototype”
    @Scope("prototype")
    public MQProducerManager defineMQProducerManager(MQNodesSync mqNodesSync, ActiveMQProperties prop) {
        MQProducerManager mqProducerManager =
            new MQProducerManager(prop.getUsername(), prop.getPassword(), prop.getQueueForProducer(), DESTTYPE.TOPIC);
        mqProducerManager.setMqNodesSync(mqNodesSync);
        return mqProducerManager;
    }

    @Bean(name = "dataSourcePool", initMethod = "init", destroyMethod = "close")
    @Lazy
    public DataSourcePool defineDataSourcePool(MysqlProperties prop) {
        DataSourcePool dataSourcePool = new DataSourcePool();

        Map<String, DruidDataSource> namedDataSources = new HashMap<>();

        for (String name : prop.getMap().keySet()) {
            DruidDataSource dataSource = new DruidDataSource();

            dataSource.setUrl(prop.getMap().get(name).getUrl());
            dataSource.setUsername(prop.getMap().get(name).getUsername());
            dataSource.setPassword(prop.getMap().get(name).getPassword());

            namedDataSources.put(name, dataSource);
        }

        dataSourcePool.setNamedDataSources(namedDataSources);

        return dataSourcePool;
    }

    @Bean(name = "jdbcTemplatePool")
    @Lazy
    public JdbcTemplatePool defineJdbcTemplatePool(DataSourcePool dataSourcePool) {
        JdbcTemplatePool jdbcTemplatePool = new JdbcTemplatePool();

        Map<String, JdbcTemplate> namedJdbcTemplates = new HashMap<>();

        for (String name : dataSourcePool.getNamedDataSources().keySet()) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePool.getNamedDataSources().get(name));

            namedJdbcTemplates.put(name, jdbcTemplate);
        }

        jdbcTemplatePool.setNamedJdbcTemplates(namedJdbcTemplates);

        return jdbcTemplatePool;
    }

}
