package cn.knqiufan.rpc.core.consumer.http;

import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/20 20:39
 */
public interface HttpInvoker {
  RpcResponse<?> post(RpcRequest rpcRequest, String url);

}
