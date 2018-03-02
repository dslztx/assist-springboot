package me.dslztx.booter.autoconfigure.zookeeper;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dslztx
 */
@ConfigurationProperties(
    prefix = "zookeeper.curator"
)
public class ZooKeeperCuratorProperties {

  String addresses;

  public String getAddresses() {
    return addresses;
  }

  public void setAddresses(String addresses) {
    this.addresses = addresses;
  }
}
