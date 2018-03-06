package me.dslztx.booter.activemq.noncluster;

import java.util.List;

public abstract class MQClientManager {

  /**
   * MQ节点用户名
   */
  protected String username;

  /**
   * MQ节点密码
   */
  protected String password;

  /**
   * Queue或者Topic的名称
   */
  protected String destination;

  /**
   * 标识是“Queue”还是“Topic”
   */
  protected DESTTYPE type;

  /**
   * 用于粘合“MQClientManager”和“ZooKeeper客户端”
   */
  protected MQNodesSync mqNodesSync;

  public MQClientManager(String username, String password, String destination, DESTTYPE type) {
    this.username = username;
    this.password = password;
    this.destination = destination;
    this.type = type;
  }

  public void init() {
    mqNodesSync.start();
  }

  public MQNodesSync getMqNodesSync() {
    return mqNodesSync;
  }

  public void setMqNodesSync(MQNodesSync mqNodesSync) {
    this.mqNodesSync = mqNodesSync;

    //反向依赖，用于回调
    this.mqNodesSync.setMQClientManager(this);
  }

  public abstract void syncMQNodes(List<String> nodes);
}
