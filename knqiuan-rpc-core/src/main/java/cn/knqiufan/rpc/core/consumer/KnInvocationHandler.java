package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.api.*;
import cn.knqiufan.rpc.core.meta.InstanceMeta;
import cn.knqiufan.rpc.core.util.MethodUtils;
import cn.knqiufan.rpc.core.util.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * 消费端的动态代理处理
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 20:03
 */
public class KnInvocationHandler implements InvocationHandler {

  private static final Logger log = LoggerFactory.getLogger(KnInvocationHandler.class);

  Class<?> service;
  RpcContext context;
  List<InstanceMeta> providers;

  public KnInvocationHandler(Class<?> service,
                             RpcContext context,
                             List<InstanceMeta> providers) {
    this.service = service;
    this.context = context;
    this.providers = providers;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) {

    if (MethodUtils.checkObjectBaseMethod(method)) {
      return null;
    }

    RpcRequest rpcRequest = new RpcRequest();
    rpcRequest.setService(service.getCanonicalName());
    rpcRequest.setMethodSign(MethodUtils.methodSign(method));
    rpcRequest.setArgs(args);

    // 超时自旋重试机制
    int reties = Integer.parseInt(context.getParameters().getOrDefault("app.reties", "1"));
    while (reties-- > 0) {
      log.debug(" ===> reties: {}", reties);
      try {
        // 前置处理
        for (Filter filter : context.getFilters()) {
          RpcResponse preResponse = filter.preFilter(rpcRequest);
          if (preResponse != null) {
            log.debug(filter.getClass().getName() + " ====> preFilter: " + preResponse);
            return castReturnResult(method, preResponse);
          }
        }

        RpcResponse<?> rpcResponse = context.getHttpInvoker().post(rpcRequest, getInstanceMeta().toUrl());

        // TODO：后置处理，拿到的可能不是最终值，需要再设计一下
        for (Filter filter : context.getFilters()) {
          rpcResponse = filter.postFilter(rpcRequest, rpcResponse);
        }

        return castReturnResult(method, rpcResponse);
      } catch (Exception ex) {
        if (!(ex.getCause() instanceof SocketTimeoutException)) {
          throw ex;
        }
      }
    }
    return null;
  }

  /**
   * 返回结果处理
   *
   * @param method      方法
   * @param rpcResponse 请求相应
   * @return 处理后结果
   */
  private static Object castReturnResult(Method method, RpcResponse<?> rpcResponse) {
    if (rpcResponse.isStatus()) {
      // 处理各种类型，包括基本类型、数组类型、对象等。
      return TypeUtils.cast(rpcResponse.getData(), method.getReturnType());
    } else {
      if (rpcResponse.getEx() instanceof RpcException ex) {
        throw ex;
      }
      throw new RpcException(rpcResponse.getEx(), RpcException.UNKNOWN_EX);
    }
  }

  /**
   * 获取服务实例
   *
   * @return 服务实例
   */
  private InstanceMeta getInstanceMeta() {
    List<InstanceMeta> instanceMetas = context.getRouter().route(providers);
    InstanceMeta instance = context.getLoadBalancer().choose(instanceMetas);
    log.debug("loadBalancer.choose(instanceMetas) ====> " + instance);
    return instance;
  }
}
