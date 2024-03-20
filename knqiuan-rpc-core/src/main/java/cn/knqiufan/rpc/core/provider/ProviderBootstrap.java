package cn.knqiufan.rpc.core.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.meta.InstanceMeta;
import cn.knqiufan.rpc.core.meta.ProviderMeta;
import cn.knqiufan.rpc.core.meta.ServiceMeta;
import cn.knqiufan.rpc.core.util.MethodUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 服务提供者启动类
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/9 1:23
 */
public class ProviderBootstrap implements ApplicationContextAware {

  ApplicationContext applicationContext;
  RegistryCenter registryCenter;

  private MultiValueMap<String, ProviderMeta> skeleton = new LinkedMultiValueMap<>();

  public MultiValueMap<String, ProviderMeta> getSkeleton() {
    return skeleton;
  }

  InstanceMeta instance;

  @Value("${server.port}")
  String port;
  @Value("${app.id}")
  String app;
  @Value("${app.namespace}")
  String namespace;
  @Value("${app.env}")
  String env;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }


  @PostConstruct
  public void init() {
    Map<String, Object> provider = applicationContext.getBeansWithAnnotation(KnProvider.class);
    registryCenter = applicationContext.getBean(RegistryCenter.class);
    provider.forEach((x, y) -> System.out.println(x));
    provider.values().forEach(this::getInterface);
  }

  /**
   * 设置 skeleton
   *
   * @param impl 被 @KnProvider 注解的服务提供者
   */
  private void getInterface(Object impl) {
    for (Class<?> anInterface : impl.getClass().getInterfaces()) {
      createProvider(impl, anInterface);
    }
  }

  /**
   * 获取请求实例字符串
   *
   * @return 请求实例字符串
   */
  private InstanceMeta getInstance() {
    String ip;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    return InstanceMeta.http(ip, Integer.valueOf(port));
  }

  /**
   * 设置桩子
   *
   * @param impl 被 KnProvider 注解的 Bean
   * @param anInterface    接口类
   */
  private void createProvider(Object impl, Class<?> anInterface) {
    for (Method method : anInterface.getMethods()) {
      if (MethodUtil.checkObjectBaseMethod(method)) {
        continue;
      }
      ProviderMeta providerMeta = new ProviderMeta();
      providerMeta.setMethod(method);
      providerMeta.setServiceImpl(impl);
      providerMeta.setMethodSign(MethodUtil.methodSign(method));
      skeleton.add(anInterface.getCanonicalName(), providerMeta);
    }

  }

  /**
   * 延迟暴露，等 SpringBoot 整个上下文运行完之后再进行注册
   */
  public void start() {
    registryCenter.start();
    instance = getInstance();
    // 注册服务
    skeleton.keySet().forEach(this::registerService);
  }

  /**
   * 服务注销，Spring 容器一销毁就执行服务注销
   */
  @PreDestroy
  public void stop() {
    skeleton.keySet().forEach(this::unregisterService);
    registryCenter.stop();
  }


  /**
   * 注册服务
   *
   * @param service 服务
   */
  private void registerService(String service) {
    ServiceMeta serviceMeta = new ServiceMeta();
    serviceMeta.setName(service);
    serviceMeta.setApp(app);
    serviceMeta.setNamespace(namespace);
    serviceMeta.setEnv(env);
    RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);
    registryCenter.register(serviceMeta, instance);
  }

  /**
   * 注销服务
   *
   * @param service 服务
   */
  private void unregisterService(String service) {
    ServiceMeta serviceMeta = new ServiceMeta();
    serviceMeta.setName(service);
    serviceMeta.setApp(app);
    serviceMeta.setNamespace(namespace);
    serviceMeta.setEnv(env);
    RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);
    registryCenter.unregister(serviceMeta, instance);
  }
}
