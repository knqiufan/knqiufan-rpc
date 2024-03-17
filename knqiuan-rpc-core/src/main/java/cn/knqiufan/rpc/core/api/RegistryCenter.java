package cn.knqiufan.rpc.core.api;

import java.util.List;

/**
 * 注册中心
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 17:53
 */
public interface RegistryCenter {

  void start();
  void stop();

  /*** provider 端 ***/
  void register(String service, String instance);
  void unregister(String service, String instance);

  /*** consumer 端 ***/
  // 拿到当前所有服务的接口
  List<String> fetchAll(String service);

  // void subscribe();
  // void heartbeat();

  /**
   * 默认 - 静态注册中心
   */
  class StaticRegistryCenter implements RegistryCenter {

    List<String> providers;

    public StaticRegistryCenter(List<String> providers) {
      this.providers = providers;
    }

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
      return providers;
    }
  }

}
