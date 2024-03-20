package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.annotation.KnConsumer;
import cn.knqiufan.rpc.core.api.LoadBalancer;
import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.api.Router;
import cn.knqiufan.rpc.core.api.RpcContext;
import cn.knqiufan.rpc.core.consumer.http.HttpInvoker;
import cn.knqiufan.rpc.core.meta.InstanceMeta;
import cn.knqiufan.rpc.core.meta.ServiceMeta;
import cn.knqiufan.rpc.core.util.MethodUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * 消费者启动类
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:47
 */
public class ConsumerBootstrap implements ApplicationContextAware, EnvironmentAware {
  @Value("${app.id}")
  String app;
  @Value("${app.namespace}")
  String namespace;
  @Value("${app.env}")
  String env;
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
   * 创建代理类 - 不使用 @PostConstruct，因为 @PostConstruct 中不能使用 getBean
   */
  public void start() {

    RpcContext context = new RpcContext();
    context.setLoadBalancer(applicationContext.getBean(LoadBalancer.class));
    context.setRouter(applicationContext.getBean(Router.class));
    context.setHttpInvoker(applicationContext.getBean(HttpInvoker.class));
    RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);

    // 获取所有bean定义的名字
    String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      // 根据名字获取到bean
      Object bean = applicationContext.getBean(beanDefinitionName);
      // 获取所有带有 KnConsumer 的 field
      List<Field> annotationFields = MethodUtil.findAnnotationField(bean.getClass(), KnConsumer.class);
      // 遍历并生成代理
      annotationFields.forEach(field -> {
        try {
          Class<?> service = field.getType();
          // 获取接口类型的全限定名称
          String serviceName = service.getCanonicalName();
          Object consumer = stub.get(serviceName);
          if (consumer == null) {
            consumer = createFromRegistry(service, context, registryCenter);
            stub.put(serviceName, consumer);
          }
          field.setAccessible(true);
          field.set(bean, consumer);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }
  }

  /**
   * 创建消费者代理
   *
   * @param service        服务器接口
   * @param context        rpc 上下文
   * @param registryCenter 注册中心
   * @return 消费者代理
   */
  private Object createFromRegistry(Class<?> service, RpcContext context, RegistryCenter registryCenter) {
    ServiceMeta serviceMeta = new ServiceMeta();
    serviceMeta.setName(service.getCanonicalName());
    serviceMeta.setApp(app);
    serviceMeta.setNamespace(namespace);
    serviceMeta.setEnv(env);
    List<InstanceMeta> providers = registryCenter.fetchAll(serviceMeta);
    System.out.println("====> map to provider: ");
    providers.forEach(System.out::println);

    // 订阅服务
    registryCenter.subscribe(serviceMeta, event -> {
      providers.clear();
      providers.addAll(event.getData());
    });
    return createConsumer(service, context, providers);
  }

  /**
   * 创建消费者代理
   *
   * @param service   消费者接口
   * @param context   rpc 上下文
   * @param providers 服务者地址组
   * @return 代理类
   */
  private Object createConsumer(Class<?> service,
                                RpcContext context,
                                List<InstanceMeta> providers) {
    return Proxy.newProxyInstance(service.getClassLoader(),
            new Class[]{service},
            new KnInvocationHandler(service, context, providers));
  }
}
