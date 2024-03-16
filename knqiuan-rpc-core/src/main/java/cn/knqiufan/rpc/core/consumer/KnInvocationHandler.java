package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 20:03
 */
public class KnInvocationHandler implements InvocationHandler {

  final static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
  Class<?> service;

  public KnInvocationHandler(Class<?> service) {
    this.service = service;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // 处理 Object 基础方法
    // Class<?> declaringClass = method.getDeclaringClass();
    // if(declaringClass == Object.class) {
    //   return method.invoke(proxy, args);
    // } else {
    //   Object o = handleObjectBaseMethod(proxy, method, args);
    //   if(o != null) {
    //     return o;
    //   }
    // }

    if (isObjectBaseMethod(method.getName())) {
      return null;
    }

    RpcRequest rpcRequest = new RpcRequest();
    rpcRequest.setService(service.getCanonicalName());
    rpcRequest.setMethod(method.getName());
    rpcRequest.setArgs(args);

    RpcResponse rpcResponse = post(rpcRequest);
    if (rpcResponse.isStatus()) {
      // TODO: 处理各种类型，包括基本类型、数组类型、对象等。
      Object data = rpcResponse.getData();
      if(data instanceof JSONObject resultJson) {
        return resultJson.toJavaObject(method.getReturnType());
      } else {
        return data;
      }

    } else {
      Exception ex = rpcResponse.getEx();
      throw new RuntimeException(ex);
    }
  }

  /**
   * 判断是否为 Object 基础方法
   */
  private boolean isObjectBaseMethod(String methodName) {
    return methodName.equals("toString") || methodName.equals("hashCode");
  }

  // /**
  //  * 处理 Object 基础方法
  //  */
  // private Object handleObjectBaseMethod(Object invoke, Method method, Object[] args) {
  //   // 获取方法参数列表
  //   Class<?>[] parameterTypes = method.getParameterTypes();
  //   if(parameterTypes.length == 0) {
  //     if("toString".equals(method.getName())) {
  //       return invoke.toString();
  //     }
  //     if("hashCode".equals(method.getName())) {
  //       return invoke.hashCode();
  //     }
  //   } else if(parameterTypes.length == 1 && "equals".equals(method.getName())) {
  //     return invoke.equals(args[0]);
  //   }
  //   return null;
  // }

  // 三种方法：OkHttpClient URLConnection HttpClient
  OkHttpClient client = new OkHttpClient.Builder()
          .connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS))
          .readTimeout(1, TimeUnit.SECONDS)
          .writeTimeout(1, TimeUnit.SECONDS)
          .connectTimeout(1, TimeUnit.SECONDS)
          .build();

  /**
   * 模拟发送post请求
   *
   * @param rpcRequest
   * @return
   */
  private RpcResponse post(RpcRequest rpcRequest) {
    String reqString = JSON.toJSONString(rpcRequest);
    Request request = new Request.Builder()
            .url("http://localhost:8080/")
            .post(RequestBody.create(reqString, JSON_TYPE))
            .build();
    try {
      String respJson = client.newCall(request).execute().body().string();
      RpcResponse rpcResponse = JSON.parseObject(respJson, RpcResponse.class);
      return rpcResponse;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
