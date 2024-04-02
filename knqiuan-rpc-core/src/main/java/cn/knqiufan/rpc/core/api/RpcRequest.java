package cn.knqiufan.rpc.core.api;

import java.util.Arrays;

/**
 * 请求模型
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:15
 */
public class RpcRequest {
  /**
   * 接口
   */
  private String service;
  /**
   * 方法
   */
  private String methodSign;
  /**
   * 参数
   */
  private Object[] args;

  public String getMethodSign() {
    return methodSign;
  }

  public void setMethodSign(String methodSign) {
    this.methodSign = methodSign;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  @Override
  public String toString() {
    return "RpcRequest{" +
            "service='" + service + '\'' +
            ", methodSign='" + methodSign + '\'' +
            ", args=" + Arrays.toString(args) +
            '}';
  }
}
