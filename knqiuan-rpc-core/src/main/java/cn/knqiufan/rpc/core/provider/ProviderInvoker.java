package cn.knqiufan.rpc.core.provider;

import cn.knqiufan.rpc.core.api.RpcException;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import cn.knqiufan.rpc.core.meta.ProviderMeta;
import cn.knqiufan.rpc.core.util.TypeUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/20 20:16
 */
public class ProviderInvoker {
  private static final Logger log = LoggerFactory.getLogger(ProviderInvoker.class);

  private MultiValueMap<String, ProviderMeta> skeleton;

  public ProviderInvoker(ProviderBootstrap providerBootstrap) {
    skeleton = providerBootstrap.getSkeleton();
  }

  /**
   * 具体执行请求方法
   *
   * @param request 请求参数
   * @return 响应参数
   */
  public RpcResponse<Object> invoke(RpcRequest request) {
    log.info("========> request: " + JSONObject.toJSONString(request));
    RpcResponse<Object> rpcResponse = new RpcResponse<>();
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
      rpcResponse.setEx(new RpcException(e.getTargetException().getMessage(), RpcException.INVOKE_EX));
    } catch (IllegalAccessException e) {
      rpcResponse.setEx(new RpcException(e.getMessage(), RpcException.UNKNOWN_EX));
    }
    log.info("========> response: " + JSONObject.toJSONString(rpcResponse));
    return rpcResponse;
  }

  /**
   * 处理参数，对必要参数进行类型转换
   *
   * @param args   反序列化后的参数
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
      if (genericParameterTypes[i] instanceof ParameterizedType parameterizedType) {
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        // 参数类型为 ArrayList，做处理
        if (args[i] instanceof ArrayList<?> originList) {
          List<Object> resultList = new ArrayList<>();
          for (Object o : originList) {
            resultList.add(TypeUtils.cast(o, (Class<?>) actualTypeArgument));
          }
          actual[i] = resultList;
          continue;
        }
        actual[i] = TypeUtils.cast(args[i], parameterTypes[i]);
        continue;
      }
      actual[i] = TypeUtils.cast(args[i], parameterTypes[i]);

    }
    return actual;
  }

  /**
   * 根据方法签名在列表查找队形服务提供者
   *
   * @param providerMetas 服务提供者列表
   * @param methodSign    方法签名
   * @return 指定服务提供者
   */
  private ProviderMeta findProviderMeta(List<ProviderMeta> providerMetas, String methodSign) {
    Optional<ProviderMeta> meta = providerMetas.stream()
            .filter(providerMeta -> providerMeta.getMethodSign().equals(methodSign))
            .findFirst();
    return meta.orElse(null);
  }
}
