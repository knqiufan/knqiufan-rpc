package cn.knqiufan.rpc.core.consumer.http;

import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import cn.knqiufan.rpc.core.consumer.http.HttpInvoker;
import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp 执行
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/20 20:40
 */
public class OkHttpInvoker implements HttpInvoker {

  final static MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

  OkHttpClient client;

  public OkHttpInvoker() {
    // 三种方法：OkHttpClient URLConnection HttpClient
    client = new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(16, 60, TimeUnit.SECONDS))
            .readTimeout(1, TimeUnit.SECONDS)
            .writeTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build();
  }

  /**
   * 模拟发送post请求
   *
   * @param rpcRequest 请求参数
   * @param url        请求地址
   * @return 响应
   */
  @Override
  public RpcResponse<?> post(RpcRequest rpcRequest, String url) {
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
