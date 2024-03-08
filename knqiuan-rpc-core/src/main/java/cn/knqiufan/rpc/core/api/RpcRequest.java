package cn.knqiufan.rpc.core.api;

/**
 * 类描述
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
  private String method;
  /**
   * 参数
   */
  private Object[] args;

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
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
}
