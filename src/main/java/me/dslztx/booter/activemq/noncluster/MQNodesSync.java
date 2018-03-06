package me.dslztx.booter.activemq.noncluster;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQNodesSync {

  private static final Logger logger = LoggerFactory.getLogger(MQNodesSync.class);

  /**
   * 与MQ节点通信的协议前缀
   */
  private static final String PROTOCAL_PREFIX = "failover:(tcp://";

  /**
   * ZooKeeper客户端
   */
  CuratorFramework curatorFramework;

  /**
   * ZooKeeper中存放MQ节点的路径
   */
  private String mqNodesPath;

  /**
   * MQ客户端管理类实例
   */
  private MQClientManager manager;

  public MQNodesSync(String mqNodesPath) {
    this.mqNodesPath = mqNodesPath;
  }

  /**
   * 给MQ节点地址和端口号增加协议前缀
   */
  private List<String> addPrototype(List<String> nodes) {
    List<String> result = new ArrayList<String>();
    for (String node : nodes) {
      result.add(PROTOCAL_PREFIX + node + ")?timeout=5000&startupMaxReconnectAttempts=3");
    }
    return result;
  }

  public CuratorFramework getCuratorFramework() {
    return curatorFramework;
  }

  public void setCuratorFramework(CuratorFramework curatorFramework) {
    this.curatorFramework = curatorFramework;
  }

  /**
   * 设置回调对象
   */
  public void setMQClientManager(MQClientManager manager) {
    this.manager = manager;
  }

  /**
   * 获取最新的MQ节点地址和端口号列表
   */
  public void start() {
    getMQNodes();
  }

  /**
   * 获取最新的MQ节点地址和端口号列表，并进行监听，能够实时回调
   */
  void getMQNodes() {
    try {
      final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,
          mqNodesPath,
          false);

      //选择“立刻缓存远端数据”
      pathChildrenCache.start(StartMode.BUILD_INITIAL_CACHE);

      fetchCurrentData(pathChildrenCache.getCurrentData());

      pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
        @Override
        public void childEvent(CuratorFramework curatorFramework,
            PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
          fetchCurrentData(pathChildrenCache.getCurrentData());
        }
      });
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  private void fetchCurrentData(List<ChildData> children) {
    if (children == null || children.size() == 0) {
      throw new RuntimeException("没有配置MQ节点");
    }

    List<String> names = new ArrayList<String>();
    for (ChildData child : children) {
      names.add(FilenameUtils.getName(child.getPath()));
    }
    manager.syncMQNodes(addPrototype(names));
  }

}
