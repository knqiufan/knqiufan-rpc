package cn.knqiufan.rpc.demo.api.service;

import cn.knqiufan.rpc.demo.api.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:03
 */
public interface UserService {
  User findById(int id);

  User findById(int id, String name);

  int getById(int id);

  long getByLongId(long id);

  long getByUser(User user);

  boolean isTrue(int id);

  double getDouble(int id);

  List<User> getList();

  List<User> getListByMap(Map<String, Integer> map);

  List<String> getStringListByMap(Map<String, User> map);

  Map<String, User> getMapListByMap(Map<String, User> map);

  Map<String, Integer> getMap();

  Map<String, Long> getMapByList(List<User> users);

  List<String> getStringListByList(List<String> strList);

  int[] getIdsByUsers(User[] user);

  int[] getIds();

  long[] getLongIds();

  int[] getIdsByIds(int[] i);

  User ex(boolean flag);

  User find(int timeout);

  void setTimeoutPorts(String timeoutPorts);
}
