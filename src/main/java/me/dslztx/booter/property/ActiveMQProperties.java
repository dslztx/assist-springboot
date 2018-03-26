package me.dslztx.booter.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "activemq")
@PropertySource("classpath:conf/activemq.properties")
public class ActiveMQProperties {

  String username;

  String password;

  String queueForProducer;

  String queueForConsumer;

  String mqNodesPath;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getQueueForProducer() {
    return queueForProducer;
  }

  public void setQueueForProducer(String queueForProducer) {
    this.queueForProducer = queueForProducer;
  }

  public String getQueueForConsumer() {
    return queueForConsumer;
  }

  public void setQueueForConsumer(String queueForConsumer) {
    this.queueForConsumer = queueForConsumer;
  }

  public String getMqNodesPath() {
    return mqNodesPath;
  }

  public void setMqNodesPath(String mqNodesPath) {
    this.mqNodesPath = mqNodesPath;
  }
}
