package cn.knqiufan.rpc.core.api;

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
