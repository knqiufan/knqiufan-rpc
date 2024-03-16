package cn.knqiufan.rpc.demo.api;

import java.util.List;

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

  boolean isTrue(int id);

  double getDouble(int id);

  List<User> getList();
}
