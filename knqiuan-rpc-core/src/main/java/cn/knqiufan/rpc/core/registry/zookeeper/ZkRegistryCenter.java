package cn.knqiufan.rpc.core.registry.zookeeper;

import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.meta.InstanceMeta;
import cn.knqiufan.rpc.core.meta.ServiceMeta;
import cn.knqiufan.rpc.core.registry.ChangedListener;
import cn.knqiufan.rpc.core.registry.Event;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * zookeeper 注册中心
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 18:08
 */
public class ZkRegistryCenter implements RegistryCenter {

  @Value("${knrpc.zkServer}")
  String servers;
  @Value("${knrpc.zkRoot}")
  String root;


  private CuratorFramework client = null;

  @Override
  public void start() {
    // 创建 zookeeper 的 client
    client = CuratorFrameworkFactory.builder()
            .connectString(servers)
            .namespace(root)
            // 重试机制 超时时间 1s 重试3次
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
    System.out.println("=======> ZkRegistryCenter starting to server[" + servers + "/" + root + "]");
    client.start();
  }

  @Override
  public void stop() {
    System.out.println(" ======> zk registry stop.");
    client.close();
  }

  @Override
  public void register(ServiceMeta service, InstanceMeta instance) {
    // 服务路径：根目录下的service
    String servicePath = "/" + service.toPath();
    // 创建服务（持久化服务节点）
    try {
      // 节点不存在则创建
      if (client.checkExists().forPath(servicePath) == null) {
        client.create().withMode(CreateMode.PERSISTENT).forPath(servicePath, "service".getBytes());
      }
      // 创建服务实例临时节点
      String instancePath = servicePath + "/" + instance.toPath();
      System.out.println("=======> register to zookeeper: " + instancePath);
      client.create().withMode(CreateMode.EPHEMERAL).forPath(instancePath, "provider".getBytes());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void unregister(ServiceMeta service, InstanceMeta instance) {
    // 服务路径：根目录下的service
    String servicePath = "/" + service.toPath();
    // 注销服务
    try {
      // 节点不存在直接返回
      if (client.checkExists().forPath(servicePath) == null) {
        return;
      }
      // 注销删除服务实例临时节点
      String instancePath = servicePath + "/" + instance.toPath();
      System.out.println("=======> unregister to zookeeper: " + instancePath);
      client.delete().quietly().forPath(instancePath);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<InstanceMeta> fetchAll(ServiceMeta service) {
    // 服务路径：根目录下的service
    String servicePath = "/" + service.toPath();
    try {
      // 获取所有子节点列表
      List<String> nodes = client.getChildren().forPath(servicePath);
      System.out.println("========> nodes: " + nodes);
      return mapInstance(nodes);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private List<InstanceMeta> mapInstance(List<String> nodes) {
    return nodes.stream().map(x -> {
      String[] url = x.split("_");
      return InstanceMeta.http(url[0], Integer.valueOf(url[1]));
    }).toList();
  }

  @Override
  public void subscribe(ServiceMeta service, ChangedListener listener) {
    // 创建树缓存
    final CuratorCache cache = CuratorCache.build(client, "/" + service.toPath());
    // 创建监听，当前路径下的节点变动会触发
    cache.listenable().addListener((type, oldChildData, newChildData) -> {
      System.out.println("========> zookeeper subscribe type: " + type + ", and child data: " + newChildData);
      List<InstanceMeta> nodes = fetchAll(service);
      listener.fire(new Event(nodes));
    });
    try {
      cache.start();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
