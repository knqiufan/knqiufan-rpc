package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.annotation.KnConsumer;
import cn.knqiufan.rpc.core.api.LoadBalancer;
import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.api.Router;
import cn.knqiufan.rpc.core.api.RpcContext;
import cn.knqiufan.rpc.core.registry.ChangedListener;
import cn.knqiufan.rpc.core.registry.Event;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:47
 */
public class ConsumerBootstrap implements ApplicationContextAware, EnvironmentAware {

  ApplicationContext applicationContext;

  Environment environment;

  // 消费端的桩子
  private Map<String, Object> stub = new HashMap<>();

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
  /**
   * 创建代理类 - @PostConstruct 中不能使用 getBean
   */
  // @PostConstruct
  public void start() {

    RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);

    RpcContext context = new RpcContext();
    context.setLoadBalancer(applicationContext.getBean(LoadBalancer.class));
    context.setRouter(applicationContext.getBean(Router.class));

    String urls = environment.getProperty("knrpc.providers");
    if(Strings.isEmpty(urls)) {
      throw new RuntimeException("knrpc.providers is empty.");
    }
    List<String> providers = List.of(urls.split(","));

    // 获取所有bean定义的名字
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      // 根据名字获取到bean
      Object bean = applicationContext.getBean(beanDefinitionName);
      // 获取所有带有 KnConsumer 的 field
      List<Field> annotationFields = findAnnotationField(bean.getClass());
      // 遍历并生成代理
      annotationFields.forEach(field -> {
        try {
          Class<?> service = field.getType();
          // 获取接口类型的全限定名称
          String serviceName = service.getCanonicalName();
          Object consumer = stub.get(serviceName);
          if (consumer == null) {
            consumer = createFromRegistry(service, context, registryCenter);
          }
          field.setAccessible(true);
          field.set(bean, consumer);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  private Object createFromRegistry(Class<?> service, RpcContext context, RegistryCenter registryCenter) {
    List<String> providers = mapProviders(registryCenter.fetchAll(service.getCanonicalName()));
    System.out.println("====> map to provider: ");
    providers.forEach(System.out::println);

    // 订阅服务
    registryCenter.subscribe(service.getCanonicalName(), event -> {
      providers.clear();
      providers.addAll(mapProviders(event.getData()));
    });
    return createConsumer(service, context, providers);
  }

  private List<String> mapProviders(List<String> nodes) {
    return nodes.stream()
            .map(x -> "http://" + x.replace('_', ':'))
            .collect(Collectors.toList());
  }

  /**
   * 创建消费者代理
   *
   * @param service      消费者接口
   * @param context       rpc 上下文
   * @param providers    服务者地址组
   * @return 代理类
   */
  private Object createConsumer(Class<?> service,
                                RpcContext context,
                                List<String> providers) {
    return Proxy.newProxyInstance(service.getClassLoader(),
            new Class[]{service},
            new KnInvocationHandler(service, context, providers));
  }

  private List<Field> findAnnotationField(Class<?> aClass) {
    List<Field> resultField = new ArrayList<>();
    while (aClass != null) {
      Field[] fields = aClass.getDeclaredFields();
      for (Field field : fields) {
        if (field.isAnnotationPresent(KnConsumer.class)) {
          resultField.add(field);
        }
      }
      aClass = aClass.getSuperclass();
    }

    return resultField;
  }

}
