package me.dslztx.booter.autoconfigure.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author dslztx
 */
@ConfigurationProperties(
    prefix = "zookeeper.curator"
)
@PropertySource(value = {"classpath:conf/zookeeper.properties", "classpath:application.properties"})
public class ZooKeeperCuratorProperties {

  String addresses;

  public String getAddresses() {
    return addresses;
  }

  public void setAddresses(String addresses) {
    this.addresses = addresses;
  }
}
