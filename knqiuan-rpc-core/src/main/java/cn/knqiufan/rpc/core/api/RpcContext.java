package cn.knqiufan.rpc.core.api;

import cn.knqiufan.rpc.core.consumer.http.HttpInvoker;
import cn.knqiufan.rpc.core.consumer.http.OkHttpInvoker;

/**
 * rpc 上下文
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 17:36
 */
public class RpcContext {
  LoadBalancer loadBalancer;
  Router router;

  HttpInvoker httpInvoker;

  public HttpInvoker getHttpInvoker() {
    return httpInvoker;
  }

  public void setHttpInvoker(HttpInvoker httpInvoker) {
    this.httpInvoker = httpInvoker;
  }

  public LoadBalancer getLoadBalancer() {
    return loadBalancer;
  }

  public void setLoadBalancer(LoadBalancer loadBalancer) {
    this.loadBalancer = loadBalancer;
  }

  public Router getRouter() {
    return router;
  }

  public void setRouter(Router router) {
    this.router = router;
  }
}
