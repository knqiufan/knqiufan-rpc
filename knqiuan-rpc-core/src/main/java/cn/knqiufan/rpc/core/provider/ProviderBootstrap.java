package cn.knqiufan.rpc.core.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public Map<String, Object> getSkeleton() {
    return skeleton;
  }

  public void setSkeleton(Map<String, Object> skeleton) {
    this.skeleton = skeleton;
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
    Object bean = skeleton.get(request.getService());
    try {
      Method method = findMethod(bean.getClass(), request.getMethod());
      Object result = method.invoke(bean, request.getArgs());
      return new RpcResponse(true, result);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private Method findMethod(Class<?> aClass, String methodNaem) {
    for (Method method : aClass.getMethods()) {
      if(method.getName().equals(methodNaem)) {
        return method;
      }
    }
    return null;
  }
}
