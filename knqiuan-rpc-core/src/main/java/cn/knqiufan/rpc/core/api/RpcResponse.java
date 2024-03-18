package cn.knqiufan.rpc.core.api;

/**
 * 响应模型
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:15
 */
public class RpcResponse<T> {
  /**
   * 状态
   */
  private boolean status;
  /**
   * 数据对象
   */
  private T data;

  private Exception ex;


  public RpcResponse() {
  }

  public RpcResponse(boolean status, T data) {
    this.status = status;
    this.data = data;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public Exception getEx() {
    return ex;
  }

  public void setEx(Exception ex) {
    this.ex = ex;
  }
}
