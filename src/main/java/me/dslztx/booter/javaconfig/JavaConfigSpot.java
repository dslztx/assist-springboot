package me.dslztx.booter.javaconfig;

import me.dslztx.booter.activemq.noncluster.DESTTYPE;
import me.dslztx.booter.activemq.noncluster.MQConsumerManager;
import me.dslztx.booter.activemq.noncluster.MQNodesSync;
import me.dslztx.booter.activemq.noncluster.MQProducerManager;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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

  @Value("${zookeeper.mqNodesPath}")
  String mqNodesPath;

  @Value("${activemq.username}")
  String username;

  @Value("${activemq.password}")
  String password;

  @Value("${activemq.producer.queue}")
  String queueForProducer;

  @Value("${activemq.consumer.queue}")
  String queueForConsumer;

  @Bean(name = "mqNodesSync")
  @Scope("prototype")
  public MQNodesSync defineMQNodesSync(CuratorFramework curatorFramework) {
    MQNodesSync mqNodesSync = new MQNodesSync(mqNodesPath);
    mqNodesSync.setCuratorFramework(curatorFramework);
    return mqNodesSync;
  }

  @Bean(name = "mqConsumerManager", initMethod = "init", destroyMethod = "close")
  //ActiveMQ客户端“not thread safe”，因此设置为“prototype”
  @Scope("prototype")
  public MQConsumerManager defineMQConsumerManager(MQNodesSync mqNodesSync) {
    MQConsumerManager mqConsumerManager = new MQConsumerManager(username, password,
        queueForConsumer,
        DESTTYPE.QUEUE);
    mqConsumerManager.setMqNodesSync(mqNodesSync);
    return mqConsumerManager;
  }

  @Bean(name = "mqProducerManager", initMethod = "init", destroyMethod = "close")
  //ActiveMQ客户端“not thread safe”，因此设置为“prototype”
  @Scope("prototype")
  public MQProducerManager defineMQProducerManager(MQNodesSync mqNodesSync) {
    MQProducerManager mqProducerManager = new MQProducerManager(username, password,
        queueForProducer,
        DESTTYPE.TOPIC);
    mqProducerManager.setMqNodesSync(mqNodesSync);
    return mqProducerManager;
  }

}
