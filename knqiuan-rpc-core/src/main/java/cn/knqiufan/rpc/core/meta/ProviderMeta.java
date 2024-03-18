package cn.knqiufan.rpc.core.meta;

import java.lang.reflect.Method;

/**
 * 服务提供者信息
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/16 18:46
 */
public class ProviderMeta {

  Method method;
  String methodSign;
  Object serviceImpl;

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }

  public String getMethodSign() {
    return methodSign;
  }

  public void setMethodSign(String methodSign) {
    this.methodSign = methodSign;
  }

  public Object getServiceImpl() {
    return serviceImpl;
  }

  public void setServiceImpl(Object serviceImpl) {
    this.serviceImpl = serviceImpl;
  }
}
