package cn.knqiufan.rpc.demo.api;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:03
 */
public interface UserService {
  User findById(int id);

  int getById(int id);
}
