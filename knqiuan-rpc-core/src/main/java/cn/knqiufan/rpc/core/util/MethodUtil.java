package cn.knqiufan.rpc.core.util;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 方法工具
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/16 18:24
 */
public class MethodUtil {

  private static final String[] objectBaseMethods = new String[]{
          "toString",
          "hashCode",
          "equals",
          "clone",
          "notify",
          "notifyAll"
  };

  /**
   * 验证是否为 Object 内置方法
   *
   * @param methodName 方法名
   * @return 是否为 Object 内置方法
   */
  public static boolean checkObjectBaseMethod(final String methodName) {
    return Arrays.asList(objectBaseMethods).contains(methodName);
  }

  /**
   * 验证是否为 Object 内置方法
   *
   * @param method 方法
   * @return 是否为 Object 内置方法
   */
  public static boolean checkObjectBaseMethod(Method method) {
    return method.getDeclaringClass().equals(Object.class);
  }

  /**
   * 获取方法签名
   *
   * @param method 方法
   * @return 方法签名
   */
  public static String methodSign(Method method) {
    StringBuilder sb = new StringBuilder(method.getName());
    sb.append("@").append(method.getParameterCount());
    Arrays.stream(method.getParameterTypes()).forEach(m -> sb.append("_").append(m.getCanonicalName()));
    return sb.toString();
  }
}
