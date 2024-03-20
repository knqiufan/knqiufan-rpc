package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.api.*;
import cn.knqiufan.rpc.core.util.MethodUtil;
import cn.knqiufan.rpc.core.util.TypeUtil;
import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
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
  RpcContext context;
  List<String> providers;

  public KnInvocationHandler(Class<?> service,
                             RpcContext context,
                             List<String> providers) {
    this.service = service;
    this.context = context;
    this.providers = providers;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (MethodUtil.checkObjectBaseMethod(method)) {
      return null;
    }

    RpcRequest rpcRequest = new RpcRequest();
    rpcRequest.setService(service.getCanonicalName());
    rpcRequest.setMethodSign(MethodUtil.methodSign(method));
    rpcRequest.setArgs(args);

    RpcResponse<Object> rpcResponse = post(rpcRequest, getUrl());
    if (rpcResponse.isStatus()) {
      // 处理各种类型，包括基本类型、数组类型、对象等。
      return TypeUtil.cast(rpcResponse.getData(), method.getReturnType());
    } else {
      Exception ex = rpcResponse.getEx();
      throw new RuntimeException(ex);
    }
  }

  private String getUrl() {
    List<String> route = context.getRouter().route(providers);
    return (String) context.getLoadBalancer().choose(route);
  }

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
   * @param rpcRequest 请求参数
   * @param url        请求地址
   * @return 响应
   */
  private RpcResponse<Object> post(RpcRequest rpcRequest, String url) {
    String reqString = JSON.toJSONString(rpcRequest);
    Request request = new Request.Builder()
            .url(url)
            .post(RequestBody.create(reqString, JSON_TYPE))
            .build();
    try {
      String respJson = client.newCall(request).execute().body().string();
      return JSON.parseObject(respJson, RpcResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
