package cn.knqiufan.rpc.core.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import cn.knqiufan.rpc.core.meta.ProviderMeta;
import cn.knqiufan.rpc.core.util.MethodUtil;
import cn.knqiufan.rpc.core.util.TypeUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 服务提供者启动类
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/9 1:23
 */
public class ProviderBootstrap implements ApplicationContextAware {

  ApplicationContext applicationContext;

  private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();


  String instance;

  @Value("${server.port}")
  String port;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }


  @PostConstruct
  public void init() {
    Map<String, Object> provider = applicationContext.getBeansWithAnnotation(KnProvider.class);
    provider.forEach((x, y) -> System.out.println(x));
    provider.values().forEach(this::getInterface);
  }

  /**
   * 延迟暴露，等 SpringBoot 整个上下文运行完之后再进行注册
   */
  public void start() {
    instance = getInstance();
    // 注册服务
    skeleton.keySet().forEach(this::registerService);
  }

  @PreDestroy
  public void stop() {
    skeleton.keySet().forEach(this::unregisterService);
  }

  private void unregisterService(String service) {
    RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);
    registryCenter.unregister(service, instance);
  }

  private void registerService(String service) {
    RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);
    registryCenter.register(service, instance);
  }

  private void getInterface(Object knProviderBean) {
    for (Class<?> anInterface : knProviderBean.getClass().getInterfaces()) {
      setSkeleton(knProviderBean, anInterface);
    }
  }

  private String getInstance() {
    String ip;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    return ip + "_" + port;
  }

  /**
   * 设置桩子
   *
   * @param knProviderBean 被 KnProvider 注解的 Bean
   * @param anInterface    接口类
   */
  private void setSkeleton(Object knProviderBean, Class<?> anInterface) {
    for (Method method : anInterface.getMethods()) {
      if (MethodUtil.checkObjectBaseMethod(method)) {
        continue;
      }
      ProviderMeta providerMeta = new ProviderMeta();
      providerMeta.setMethod(method);
      providerMeta.setServiceImpl(knProviderBean);
      providerMeta.setMethodSign(MethodUtil.methodSign(method));
      skeleton.add(anInterface.getCanonicalName(), providerMeta);
    }

  }

  public RpcResponse invoke(RpcRequest request) {
    System.out.println("========> request: " + JSONObject.toJSONString(request));
    RpcResponse rpcResponse = new RpcResponse();
    List<ProviderMeta> providerMetas = skeleton.get(request.getService());
    try {
      ProviderMeta providerMeta = findProviderMeta(providerMetas, request.getMethodSign());
      // 需要对 request.getArgs() 进行类型转换。fastJson 序列化会改变参数类型
      Object[] args = processArgs(request.getArgs(), providerMeta.getMethod());
      Object result = providerMeta.getMethod().invoke(providerMeta.getServiceImpl(), args);
      rpcResponse.setStatus(true);
      rpcResponse.setData(result);
    } catch (InvocationTargetException e) {
      // 反射目标异常
      rpcResponse.setEx(new RuntimeException(e.getTargetException().getMessage()));
    } catch (IllegalAccessException e) {
      rpcResponse.setEx(new RuntimeException(e.getMessage()));
    }
    System.out.println("========> response: " + JSONObject.toJSONString(rpcResponse));
    return rpcResponse;
  }

  /**
   * 处理参数，对必要参数进行类型转换
   *
   * @param args           反序列化后的参数
   * @param method 原参数类型列表
   * @return 原参数
   */
  private Object[] processArgs(Object[] args, Method method) {
    // 获取方法参数类型
    Type[] genericParameterTypes = method.getGenericParameterTypes();
    Class<?>[] parameterTypes = method.getParameterTypes();
    if (args == null || args.length == 0) return args;
    Object[] actual = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      // 若具有参数化的类型，获取实际类型
      if(genericParameterTypes[i] instanceof ParameterizedType parameterizedType) {
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        // 参数类型为 ArrayList，做处理
        if (args[i] instanceof ArrayList<?> originList) {
          List<Object> resultList = new ArrayList<>();
          for (Object o : originList) {
            resultList.add(TypeUtil.cast(o, (Class<?>) actualTypeArgument));
          }
          actual[i] = resultList;
          continue;
        }
        actual[i] = TypeUtil.cast(args[i], parameterTypes[i]);
        continue;
      }
      actual[i] = TypeUtil.cast(args[i], parameterTypes[i]);

    }
    return actual;
  }



  private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
    Optional<ProviderMeta> meta = providerMetas.stream()
            .filter(providerMeta -> providerMeta.getMethodSign().equals(methodSign))
            .findFirst();
    return meta.orElse(null);
  }
}
