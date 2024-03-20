package cn.knqiufan.rpc.core.api;

import cn.knqiufan.rpc.core.meta.InstanceMeta;
import cn.knqiufan.rpc.core.meta.ServiceMeta;
import cn.knqiufan.rpc.core.registry.ChangedListener;

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
  void register(ServiceMeta service, InstanceMeta instance);
  void unregister(ServiceMeta service, InstanceMeta instance);

  /*** consumer 端 ***/
  // 拿到当前所有服务的接口
  List<InstanceMeta> fetchAll(ServiceMeta service);

  /**
   * 订阅
   */
  void subscribe(ServiceMeta service, ChangedListener listener);
  // void heartbeat();

  /**
   * 默认 - 静态注册中心
   */
  class StaticRegistryCenter implements RegistryCenter {

    List<InstanceMeta> providers;

    public StaticRegistryCenter(List<InstanceMeta> providers) {
      this.providers = providers;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void register(ServiceMeta service, InstanceMeta instance) {

    }

    @Override
    public void unregister(ServiceMeta service, InstanceMeta instance) {

    }

    @Override
    public List<InstanceMeta> fetchAll(ServiceMeta service) {
      return providers;
    }

    @Override
    public void subscribe(ServiceMeta service, ChangedListener listener) {

    }
  }

}
