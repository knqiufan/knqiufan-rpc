package cn.knqiufan.rpc.core.registry;

import cn.knqiufan.rpc.core.api.RegistryCenter;

import java.util.List;

/**
 * zookeeper 注册中心
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 18:08
 */
public class ZkRegistryCenter implements RegistryCenter {
  @Override
  public void start() {

  }

  @Override
  public void stop() {

  }

  @Override
  public void register(String service, String instance) {

  }

  @Override
  public void unregister(String service, String instance) {

  }

  @Override
  public List<String> fetchAll(String service) {
    return null;
  }
}
