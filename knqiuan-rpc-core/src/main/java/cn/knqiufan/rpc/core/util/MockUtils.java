package cn.knqiufan.rpc.core.util;

import java.lang.reflect.Field;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/2 10:26
 */
public class MockUtils {

  public static Object mock(Class<?> type) {
    if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
      return 1;
    } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
      return 100000L;
    }
    if (Number.class.isAssignableFrom(type)) {
      return 1;
    }
    if (type.equals(String.class)) {
      return "this is a mock string";
    }
    return mockPojo(type);
  }

  private static Object mockPojo(Class<?> type) {
    Object result;
    try {
      result = type.getDeclaredConstructor().newInstance();
      for (Field field : type.getDeclaredFields()) {
        field.setAccessible(true);
        field.set(result, mock(field.getType()));
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  public static void main(String[] args) {
    System.out.println(mock(UserDto.class));
  }

  public static class UserDto {
    private int a;
    private String b;

    @Override
    public String toString() {
      return "UserDto{" +
              "a=" + a +
              ", b='" + b + '\'' +
              '}';
    }
  }
}
