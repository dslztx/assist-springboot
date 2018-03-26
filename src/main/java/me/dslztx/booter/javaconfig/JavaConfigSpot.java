package me.dslztx.booter.javaconfig;

import me.dslztx.booter.activemq.noncluster.DESTTYPE;
import me.dslztx.booter.activemq.noncluster.MQConsumerManager;
import me.dslztx.booter.activemq.noncluster.MQNodesSync;
import me.dslztx.booter.activemq.noncluster.MQProducerManager;
import me.dslztx.booter.property.ActiveMQProperties;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Bean(name = "mqNodesSync")
  @Scope("prototype")
  public MQNodesSync defineMQNodesSync(CuratorFramework curatorFramework, ActiveMQProperties prop) {
    MQNodesSync mqNodesSync = new MQNodesSync(prop.getMqNodesPath());
    mqNodesSync.setCuratorFramework(curatorFramework);
    return mqNodesSync;
  }

  @Bean(name = "mqConsumerManager", initMethod = "init", destroyMethod = "close")
  //ActiveMQ客户端“not thread safe”，因此设置为“prototype”
  @Scope("prototype")
  public MQConsumerManager defineMQConsumerManager(MQNodesSync mqNodesSync,
      ActiveMQProperties prop) {
    MQConsumerManager mqConsumerManager = new MQConsumerManager(prop.getUsername(),
        prop.getPassword(), prop.getQueueForConsumer(), DESTTYPE.QUEUE);
    mqConsumerManager.setMqNodesSync(mqNodesSync);
    return mqConsumerManager;
  }

  @Bean(name = "mqProducerManager", initMethod = "init", destroyMethod = "close")
  //ActiveMQ客户端“not thread safe”，因此设置为“prototype”
  @Scope("prototype")
  public MQProducerManager defineMQProducerManager(MQNodesSync mqNodesSync,
      ActiveMQProperties prop) {
    MQProducerManager mqProducerManager = new MQProducerManager(prop.getUsername(),
        prop.getPassword(), prop.getQueueForProducer(), DESTTYPE.TOPIC);
    mqProducerManager.setMqNodesSync(mqNodesSync);
    return mqProducerManager;
  }

}
