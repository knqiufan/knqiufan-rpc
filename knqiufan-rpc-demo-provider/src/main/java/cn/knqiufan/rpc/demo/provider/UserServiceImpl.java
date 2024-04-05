package cn.knqiufan.rpc.demo.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.demo.api.pojo.User;
import cn.knqiufan.rpc.demo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户测试类
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:06
 */
@Component
@KnProvider
public class UserServiceImpl implements UserService {

  @Autowired
  Environment environment;

  @Override
  public User findById(int id) {
    return new User(id, "knqiufan-" + environment.getProperty("server.port") + "_" + System.currentTimeMillis());
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
  public long getByLongId(long id) {
    return id;
  }

  @Override
  public long getByUser(User user) {
    return user.getId().longValue();
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
    return new ArrayList<>() {{
      add(new User(1, "knqiufan"));
      add(new User(2, "maidou"));
    }};
  }

  @Override
  public List<User> getListByMap(Map<String, Integer> map) {
    return new ArrayList<>() {{
      add(new User(343434, "33"));
    }};
  }

  @Override
  public List<String> getStringListByMap(Map<String, User> map) {
    return new ArrayList<>(map.keySet());
  }

  @Override
  public Map<String, User> getMapListByMap(Map<String, User> map) {
    return map;
  }

  @Override
  public Map<String, Integer> getMap() {
    return new HashMap<>() {{
      put("1", 23);
    }};
  }

  @Override
  public Map<String, Long> getMapByList(List<User> users) {
    Map<String, Long> map = new HashMap<>();
    users.forEach(u -> map.put(u.getName(), u.getId().longValue()));
    return map;
  }

  @Override
  public List<String> getStringListByList(List<String> strList) {
    return strList;
  }

  @Override
  public int[] getIdsByUsers(User[] user) {
    return new int[]{user[0].getId()};
  }

  @Override
  public int[] getIds() {
    return new int[]{1, 2};
  }

  @Override
  public long[] getLongIds() {
    return new long[]{4, 3, 6};
  }

  @Override
  public int[] getIdsByIds(int[] i) {
    return i;
  }

  @Override
  public User ex(boolean flag) {
    if(flag) throw new RuntimeException("just throw an exception");
    return new User(100, "knqiufan");
  }

  @Override
  public User find(int timeout) {
    String port = environment.getProperty("server.port");
    if("8081".equals(port)) {
      try {
        Thread.sleep(timeout);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    return new User(2233, "knqiufan");
  }
}
