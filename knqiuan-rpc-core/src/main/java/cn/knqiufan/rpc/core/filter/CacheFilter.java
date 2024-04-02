package cn.knqiufan.rpc.core.filter;

import cn.knqiufan.rpc.core.api.Filter;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存过滤器
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/2 9:25
 */
public class CacheFilter implements Filter {
  // TODO：替换成 guava cache 加容量和过期时间
  static Map<String, RpcResponse> cache = new ConcurrentHashMap();
  @Override
  public RpcResponse preFilter(RpcRequest request) {
    return cache.get(request.toString());
  }

  @Override
  public RpcResponse postFilter(RpcRequest request, RpcResponse response) {
    cache.putIfAbsent(request.toString(), response);
    return response;
  }

  @Override
  public Filter next() {
    return null;
  }
}
