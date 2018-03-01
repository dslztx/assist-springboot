package me.dslztx.booter.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.curator.utils.CloseableUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * curator框架透明处理“与ZooKeeper的connection和session的断开重连”等情况，大大降低编程复杂度<br/>
 *
 * NodeCache和PathChildrenCache原理：本地缓存远端数据，每隔一段时间比对本地缓存数据与远端最新数据，如果不同触发1次监听器调用。因此，
 * 是“主动侦询”，不是“被动调用”，在“被动调用”下，N次改动会触发N次监听器调用，在“主动侦询”下，如果N次改动在一个主动侦询周期内，则也只会触发1次监听器调用
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

      //立刻缓存远端数据
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
}
