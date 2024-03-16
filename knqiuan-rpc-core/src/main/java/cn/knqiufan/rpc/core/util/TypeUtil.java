package cn.knqiufan.rpc.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/16 19:41
 */
public class TypeUtil {

  /**
   * 类型转换
   *
   * @param origin 原始对象
   * @param type   转换类型
   * @return 转换后的对象
   */
  public static Object cast(Object origin, Class<?> type) {
    if (origin == null) return null;
    Class<?> aClass = origin.getClass();
    // 转换的类型如果是原始对象类型的父类，直接返回原始对象
    if (type.isAssignableFrom(aClass)) {
      return origin;
    }
    // hashMap 转对象
    if (origin instanceof HashMap map) {
      JSONObject jsonObject = new JSONObject(map);
      return jsonObject.toJavaObject(type);
    }
    if(origin instanceof JSONObject resultJson) {
      return resultJson.toJavaObject(type);
    }
    // 数组类型的转换
    if (origin instanceof JSONArray originJsonArray) {
      return handleArray(originJsonArray.toArray(), type);
    }

    if (origin instanceof ArrayList<?> originList) {
      return handleArray(originList.toArray(), type);
    }
    // 基本类型
    if (type.equals(Long.class) || type.equals(Long.TYPE)) {
      return Long.valueOf(origin.toString());
    } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
      return Integer.valueOf(origin.toString());
    } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
      return Double.valueOf(origin.toString());
    } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
      return Float.valueOf(origin.toString());
    } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
      return Boolean.valueOf(origin.toString());
    } else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
      return Character.valueOf(origin.toString().charAt(0));
    } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
      return Short.valueOf(origin.toString());
    } else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
      return Byte.valueOf(origin.toString());
    }
    return null;
  }

  /**
   * 处理数组类型
   *
   * @param originArray 原始数组
   * @param type        数组类型
   * @return 处理后参数
   */
  private static Object handleArray(Object[] originArray, Class<?> type) {
    // 获取数组类型
    Class<?> componentType = type.getComponentType();
    // 根据数组类型创建数组
    Object resultArray = Array.newInstance(componentType, originArray.length);
    // 往数组中插入数据
    for (int i = 0; i < originArray.length; i++) {
      Array.set(resultArray, i, originArray[i]);
    }
    return resultArray;
  }
}
