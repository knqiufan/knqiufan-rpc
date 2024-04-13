package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.api.*;
import cn.knqiufan.rpc.core.governance.SlidingTimeWindow;
import cn.knqiufan.rpc.core.meta.InstanceMeta;
import cn.knqiufan.rpc.core.util.MethodUtils;
import cn.knqiufan.rpc.core.util.TypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
  final List<InstanceMeta> providers;
  // 隔离的服务提供者
  final List<InstanceMeta> isolateProviders = new ArrayList<>();
  // 半开的服务提供者
  final List<InstanceMeta> halfOpenProviders = new ArrayList<>();
  ScheduledExecutorService executor;
  Map<String, SlidingTimeWindow> windows = new HashMap<>();

  public KnInvocationHandler(Class<?> service,
                             RpcContext context,
                             List<InstanceMeta> providers) {
    this.service = service;
    this.context = context;
    this.providers = providers;
    this.executor = Executors.newScheduledThreadPool(1);
    this.executor.scheduleWithFixedDelay(this::halfOpen, 10, 60, TimeUnit.SECONDS);
  }

  private void halfOpen() {
    log.debug(" ====> half open isolateProviders: {}", isolateProviders);
    halfOpenProviders.clear();
    halfOpenProviders.addAll(isolateProviders);
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

        // 探活
        InstanceMeta instance;
        synchronized (halfOpenProviders) {
          // 若半开的服务提供者为空，则直接从可用的服务提供者中获取
          if (halfOpenProviders.isEmpty()) {
            instance = getInstanceMeta();
          } else {
            // 若半开的服务提供者不为空，则从半开的服务提供者中获取进行探活
            instance = halfOpenProviders.remove(0);
            log.debug("check alive instance ====> {}", instance);
          }
        }


        log.debug("loadBalancer.choose(instances) ====> {}", instance);

        RpcResponse<?> rpcResponse;
        String url = instance.toUrl();

        try {
          rpcResponse = context.getHttpInvoker().post(rpcRequest, url);
        } catch (Exception e) {
          // 故障的规则统计和隔离
          // 每一次异常，记录一次，统计 30s 的异常数据
          // 加 synchronized 防止多线程问题
          synchronized (providers) {
            SlidingTimeWindow window = windows.get(url);
            if (window == null) {
              window = new SlidingTimeWindow();
              windows.put(url, window);
            }
            window.record(System.currentTimeMillis());
            log.debug("instance {} in window with {}", url, window.getSum());
            // 若发生了10次，就故障隔离
            if (window.getSum() >= 10) {
              isolate(instance);
            }
          }
          throw e;
        }

        synchronized (providers) {
          // 如果调用成功，表示探活成功
          if (!providers.contains(instance)) {
            isolateProviders.remove(instance);
            providers.add(instance);
            log.debug("instance {} is recovered, isolateProviders={}, providers={}", instance, isolateProviders, providers);
          }
        }
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

  private void isolate(InstanceMeta instance) {
    log.debug("====> isolate instance {}", instance);
    // 故障隔离，移除provider
    providers.remove(instance);
    log.debug("====> providers: {}", providers);
    isolateProviders.add(instance);
    log.debug("====> isolateProviders: {}", isolateProviders);
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
