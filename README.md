## **一、ZooKeeper客户端**
在“application.properties”中配置`zookeeper.addresses`参数，通过如下代码注入ZooKeeper的Curator客户端：
```
@Autowired
CuratorFramework curatorClient;
```
