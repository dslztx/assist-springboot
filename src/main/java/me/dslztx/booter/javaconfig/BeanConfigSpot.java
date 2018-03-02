package me.dslztx.booter.javaconfig;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dslztx
 */
@Configuration
public class BeanConfigSpot {

  @Value("${zookeeper.addresses:#{null}}")
  String zookeeperAddresses;

  @Bean(name = "curatorFramework", initMethod = "start", destroyMethod = "close")
  public CuratorFramework defineCuratorFramework() {
    if (zookeeperAddresses == null || zookeeperAddresses.length() == 0) {
      return null;
    }
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    return CuratorFrameworkFactory
        .newClient(zookeeperAddresses, retryPolicy);
  }
}
