package me.dslztx.booter.zookeeper;

import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * curator框架透明处理“与ZooKeeper的connection和session的断开重连”等情况，大大降低编程复杂度<br/>
 *
 * NodeCache和PathChildrenCache原理：本地缓存远端数据，每隔一段时间比对本地缓存数据与远端最新数据，如果不同触发1次监听器调用。因此，
 * 是“主动侦询”，不是“被动调用”，在“被动调用”下，N次改动会触发N次监听器调用，在“主动侦询”下，如果N次改动在一个主动侦询周期内，则也只会触发1次监听器调用
 *
 * @author dslztx
 */
public class ZooKeeperTest {

  private static final Logger logger = LoggerFactory.getLogger(ZooKeeperTest.class);

  @Test
  public void nodeCacheTest() {
    try {
      //Mock ZooKeeper Server
      TestingServer server = new TestingServer();

      CuratorFramework client = CuratorFrameworkFactory
          .newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
      client.start();

      String path = "/";

      final NodeCache nodeCache = new NodeCache(client, path);

      //选择“立刻缓存远端数据”
      nodeCache.start(true);

      nodeCache.getListenable().addListener(new NodeCacheListener() {
        @Override
        public void nodeChanged() throws Exception {
          System.out.println("监听事件触发");
          System.out.println("获取节点最新内容为：" + new String(nodeCache.getCurrentData().getData()));
        }
      });

      //如果在同一个“侦询周期”内，可能只打印“789”
      client.setData().forPath(path, "123".getBytes());
      client.setData().forPath(path, "456".getBytes());
      client.setData().forPath(path, "789".getBytes());

      //尽量保证能够等到一个“侦询周期”的结束
      Thread.sleep(5000);

      client.setData().forPath(path, "012".getBytes());

      //尽量保证能够等到一个“侦询周期”的结束
      Thread.sleep(5000);

      CloseableUtils.closeQuietly(nodeCache);
      CloseableUtils.closeQuietly(client);
      CloseableUtils.closeQuietly(server);
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  @Test
  public void pathChildrenCacheTest() {
    try {
      //Mock ZooKeeper Server
      TestingServer server = new TestingServer();

      CuratorFramework client = CuratorFrameworkFactory
          .newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
      client.start();

      String path = "/";

      final PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, false);

      //选择“立刻缓存远端数据”
      pathChildrenCache.start(StartMode.BUILD_INITIAL_CACHE);

      pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
        @Override
        public void childEvent(CuratorFramework curatorFramework,
            PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {

          System.out.println("监听事件触发，获取最新子节点内容");
          List<ChildData> childDataList = pathChildrenCache.getCurrentData();
          if (childDataList != null && childDataList.size() != 0) {
            for (ChildData childData : childDataList) {
              System.out.println("子节点：" + childData);
            }
          }
        }
      });

      //如果在同一个“侦询周期”内，只会触发1次监听器调用，否则会触发多次
      client.create().forPath(path + "123");
      client.create().forPath(path + "456");
      client.create().forPath(path + "789");

      //尽量保证能够等到一个“侦询周期”的结束
      Thread.sleep(5000);

      client.create().forPath(path + "012");

      //尽量保证能够等到一个“侦询周期”的结束
      Thread.sleep(5000);

      CloseableUtils.closeQuietly(pathChildrenCache);
      CloseableUtils.closeQuietly(client);
      CloseableUtils.closeQuietly(server);
    } catch (Exception e) {
      logger.error("", e);
    }
  }

  @Test
  public void createEPHEMERALNodeTest() {
    try {
      //Mock ZooKeeper Server
      TestingServer server = new TestingServer();

      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            CuratorFramework client = CuratorFrameworkFactory
                .newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
            client.start();

            client.create().withMode(CreateMode.EPHEMERAL).forPath("/curator");

            //会话保持5000ms
            Thread.sleep(5000);

            CloseableUtils.closeQuietly(client);
          } catch (Exception e) {
            logger.error("", e);
          }
        }
      }).start();

      //确保上面的线程先执行
      Thread.sleep(1000);

      CuratorFramework client = CuratorFrameworkFactory
          .newClient(server.getConnectString(), new ExponentialBackoffRetry(1000, 3));
      client.start();

      Assert.assertNotNull(client.checkExists().forPath("/curator"));

      //确保会话结束，临时节点删除
      Thread.sleep(7000);

      Assert.assertNull(client.checkExists().forPath("/curator"));

      CloseableUtils.closeQuietly(client);
      CloseableUtils.closeQuietly(server);
    } catch (Exception e) {
      logger.error("", e);
    }
  }
}
