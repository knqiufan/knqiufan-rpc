package cn.knqiufan.rpc.demo.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.demo.api.User;
import cn.knqiufan.rpc.demo.api.UserService;
import org.springframework.stereotype.Component;

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
  public int getById(int id) {
    return id;
  }
}
