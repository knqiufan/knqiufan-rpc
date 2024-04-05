package cn.knqiufan.rpc.core.api;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/5 16:04
 */
public class KnrpcException extends  RuntimeException{

  private String errorCode;

  // X => 技术类异常
  // Y => 业务类异常
  // Z => unknown 异常
  public static final String SOCKET_TIMEOUT_EX = "X001-HTTP_INVOKE_TIMEOUT";
  public static final String NO_SUCH_METHOD_EX = "X002-METHOD_NO_EXISTS";
  public static final String UNKNOWN_EX = "Z001-UNKNOWN";


  public KnrpcException() {
  }

  public KnrpcException(String errorCode) {
    this.errorCode = errorCode;
  }

  public KnrpcException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public KnrpcException(String message, Throwable cause, String errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public KnrpcException(Throwable cause, String errorCode) {
    super(cause);
    this.errorCode = errorCode;
  }

  public KnrpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String errorCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
}
