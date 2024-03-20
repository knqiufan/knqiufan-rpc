package cn.knqiufan.rpc.core.api;

import cn.knqiufan.rpc.core.consumer.http.HttpInvoker;
import cn.knqiufan.rpc.core.consumer.http.OkHttpInvoker;
import cn.knqiufan.rpc.core.meta.InstanceMeta;

/**
 * rpc 上下文
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 17:36
 */
public class RpcContext {
  LoadBalancer<InstanceMeta> loadBalancer;
  Router<InstanceMeta> router;

  HttpInvoker httpInvoker;

  public HttpInvoker getHttpInvoker() {
    return httpInvoker;
  }

  public void setHttpInvoker(HttpInvoker httpInvoker) {
    this.httpInvoker = httpInvoker;
  }

  public LoadBalancer<InstanceMeta> getLoadBalancer() {
    return loadBalancer;
  }

  public void setLoadBalancer(LoadBalancer<InstanceMeta> loadBalancer) {
    this.loadBalancer = loadBalancer;
  }

  public Router<InstanceMeta> getRouter() {
    return router;
  }

  public void setRouter(Router<InstanceMeta> router) {
    this.router = router;
  }
}
