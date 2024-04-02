package cn.knqiufan.rpc.core.filter;

import cn.knqiufan.rpc.core.api.Filter;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import cn.knqiufan.rpc.core.util.MethodUtils;
import cn.knqiufan.rpc.core.util.MockUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/2 10:02
 */
public class MockFilter implements Filter {
  @Override
  public RpcResponse preFilter(RpcRequest request) {
    Method method;
    try {
      method = findMethod(Class.forName(request.getService()), request.getMethodSign());
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    return (RpcResponse) MockUtils.mock(method.getReturnType());
  }

  /**
   * 查找方法
   *
   * @param service    服务
   * @param methodSign 方法签名
   * @return 方法
   */
  private Method findMethod(Class<?> service, String methodSign) {
    return Arrays.stream(service.getMethods())
            .filter(method -> !MethodUtils.checkObjectBaseMethod(method))
            .filter(method -> methodSign.equals(MethodUtils.methodSign(method)))
            .findFirst()
            .orElse(null);
  }

  @Override
  public RpcResponse postFilter(RpcRequest request, RpcResponse response) {
    return null;
  }

  @Override
  public Filter next() {
    return null;
  }


}
