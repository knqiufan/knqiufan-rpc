package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.api.RpcContext;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import cn.knqiufan.rpc.core.consumer.http.OkHttpInvoker;
import cn.knqiufan.rpc.core.util.MethodUtil;
import cn.knqiufan.rpc.core.util.TypeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 消费端的动态代理处理
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 20:03
 */
public class KnInvocationHandler implements InvocationHandler {

  OkHttpInvoker httpInvoker = new OkHttpInvoker();

  Class<?> service;
  RpcContext context;
  List<String> providers;

  public KnInvocationHandler(Class<?> service,
                             RpcContext context,
                             List<String> providers) {
    this.service = service;
    this.context = context;
    this.providers = providers;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (MethodUtil.checkObjectBaseMethod(method)) {
      return null;
    }

    RpcRequest rpcRequest = new RpcRequest();
    rpcRequest.setService(service.getCanonicalName());
    rpcRequest.setMethodSign(MethodUtil.methodSign(method));
    rpcRequest.setArgs(args);

    RpcResponse<?> rpcResponse = httpInvoker.post(rpcRequest, getUrl());
    if (rpcResponse.isStatus()) {
      // 处理各种类型，包括基本类型、数组类型、对象等。
      return TypeUtil.cast(rpcResponse.getData(), method.getReturnType());
    } else {
      Exception ex = rpcResponse.getEx();
      throw new RuntimeException(ex);
    }
  }

  private String getUrl() {
    List<String> route = context.getRouter().route(providers);
    return (String) context.getLoadBalancer().choose(route);
  }
}
