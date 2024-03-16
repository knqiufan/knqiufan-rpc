package cn.knqiufan.rpc.core.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/9 1:23
 */
public class ProviderBootstrap implements ApplicationContextAware {

  ApplicationContext applicationContext;

  private Map<String, Object> skeleton = new HashMap<>();

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  public void buildProvider() {
    Map<String, Object> provider = applicationContext.getBeansWithAnnotation(KnProvider.class);
    provider.forEach((x, y) -> System.out.println(x));
    provider.values().forEach(this::getInterface);
  }

  private void getInterface(Object x) {
    for (Class<?> anInterface : x.getClass().getInterfaces()) {
      skeleton.put(anInterface.getCanonicalName(), x);
    }
  }

  public RpcResponse invoke(RpcRequest request) {
    if (isObjectBaseMethod(request.getMethod())) {
      return null;
    }
    RpcResponse rpcResponse = new RpcResponse();
    Object bean = skeleton.get(request.getService());
    try {
      Method method = findMethod(bean.getClass(), request.getMethod());
      Object result = method.invoke(bean, request.getArgs());
      rpcResponse.setStatus(true);
      rpcResponse.setData(result);
    } catch (InvocationTargetException e) {
      // 反射目标异常
      rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
    } catch (IllegalAccessException e) {
      rpcResponse.setEx(new RuntimeException(e.getMessage()));
    }
    return rpcResponse;
  }

  /**
   * 判断是否为 Object 基础方法
   */
  private boolean isObjectBaseMethod(String methodName) {
    return methodName.equals("toString") || methodName.equals("hashCode");
  }

  private Method findMethod(Class<?> aClass, String methodNaem) {
    for (Method method : aClass.getMethods()) {
      if (method.getName().equals(methodNaem)) {
        return method;
      }
    }
    return null;
  }
}
