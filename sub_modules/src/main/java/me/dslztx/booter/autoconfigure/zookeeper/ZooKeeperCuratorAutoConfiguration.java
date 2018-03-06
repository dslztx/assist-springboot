package me.dslztx.booter.autoconfigure.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dslztx
 */
@Configuration
@ConditionalOnClass(CuratorFramework.class)
@EnableConfigurationProperties({ZooKeeperCuratorProperties.class})
public class ZooKeeperCuratorAutoConfiguration {

  @Bean(name = "curatorFramework", initMethod = "start", destroyMethod = "close")
  @ConditionalOnProperty(prefix = "zookeeper.curator", name = "addresses")
  @ConditionalOnMissingBean(type = "org.apache.curator.framework.CuratorFramework")
  public CuratorFramework defineCuratorFramework(
      ZooKeeperCuratorProperties zooKeeperCuratorProperties) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    return CuratorFrameworkFactory
        .newClient(zooKeeperCuratorProperties.getAddresses(), retryPolicy);
  }
}
