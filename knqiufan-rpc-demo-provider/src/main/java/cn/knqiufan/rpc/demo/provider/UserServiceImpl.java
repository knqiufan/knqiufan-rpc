package cn.knqiufan.rpc.demo.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.demo.api.User;
import cn.knqiufan.rpc.demo.api.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:06
 */
@Component
@KnProvider
public class UserServiceImpl implements UserService {
  @Override
  public User findById(int id) {
    return new User(id, "knqiufan-" + System.currentTimeMillis());
  }

  @Override
  public User findById(int id, String name) {
    return new User(id, name);
  }

  @Override
  public int getById(int id) {
    return id;
  }

  @Override
  public boolean isTrue(int id) {
    return id == 33;
  }

  @Override
  public double getDouble(int id) {
    return Double.parseDouble(String.valueOf(id));
  }

  @Override
  public List<User> getList() {
    return new ArrayList<>(){{
      add(new User(1, "knqiufan"));
      add(new User(2, "maidou"));
    }};
  }
}
