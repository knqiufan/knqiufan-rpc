package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.annotation.KnConsumer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:47
 */
public class ConsumerBootstrap implements ApplicationContextAware {

  ApplicationContext applicationContext;

  // 消费端的桩子
  private Map<String, Object> stub = new HashMap<>();

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * 创建代理类 - @PostConstruct 中不能使用 getBean
   */
  // @PostConstruct
  public void start() {
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
            consumer = createConsumer(service);
          }
          field.setAccessible(true);
          field.set(bean, consumer);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
  }

  private Object createConsumer(Class<?> service) {
    return Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new KnInvocationHandler(service));
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
