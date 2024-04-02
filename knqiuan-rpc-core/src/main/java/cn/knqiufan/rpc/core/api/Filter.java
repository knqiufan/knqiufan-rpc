package cn.knqiufan.rpc.core.api;

/**
 * 过滤器 - 前置处理 后置处理
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 16:14
 */
public interface Filter {

  RpcResponse preFilter(RpcRequest request);

  RpcResponse postFilter(RpcRequest request, RpcResponse response);

  Filter next();

  Filter Default = new Filter() {
    @Override
    public RpcResponse preFilter(RpcRequest request) {
      return null;
    }

    @Override
    public RpcResponse postFilter(RpcRequest request, RpcResponse response) {
      return response;
    }

    @Override
    public Filter next() {
      return null;
    }
  };
}
