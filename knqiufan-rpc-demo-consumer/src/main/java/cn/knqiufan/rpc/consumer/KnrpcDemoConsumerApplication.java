package cn.knqiufan.rpc.consumer;

import cn.knqiufan.rpc.core.annotation.KnConsumer;
import cn.knqiufan.rpc.core.consumer.ConsumerConfig;
import cn.knqiufan.rpc.demo.api.Order;
import cn.knqiufan.rpc.demo.api.OrderService;
import cn.knqiufan.rpc.demo.api.User;
import cn.knqiufan.rpc.demo.api.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:37
 */
@SpringBootApplication
@Import({ConsumerConfig.class})
@RestController
public class KnrpcDemoConsumerApplication {

  @KnConsumer
  UserService userService;

  @KnConsumer
  OrderService orderService;

  @RequestMapping("/")
  public User findById(int id) {
    return userService.findById(id);
  }

  public static void main(String[] args) {
    SpringApplication.run(KnrpcDemoConsumerApplication.class, args);
  }

  @Bean
  public ApplicationRunner consumerRunner() {
    return x -> {
      User user = userService.findById(1);
      System.out.println(user);
      // System.out.println(userService.getByUser(new User(1, "knqiufan")));
      // System.out.println(userService.getByLongId(1));
      //
      //
      // User maidou = userService.findById(2, "maidou");
      // System.out.println(maidou);
      // System.out.println(userService.getById(22));
      // System.out.println(userService.toString());
      // System.out.println(userService.getDouble(33));
      // System.out.println(userService.isTrue(4));
      // System.out.println(userService.getList());
      // System.out.println(Arrays.toString(userService.getIds()));
      // System.out.println(Arrays.toString(userService.getLongIds()));
      // System.out.println(Arrays.toString(userService.getIdsByIds(new int[]{99, 55})));
      //
      // System.out.println(userService.getMap());
      // System.out.println(userService.getListByMap(new HashMap<>() {{
      //   put("2", 4);
      // }}));
      // System.out.println(userService.getStringListByMap(new HashMap<>() {{
      //   put("22", new User(88, "knqiufan"));
      // }}));
      // System.out.println(userService.getMapListByMap(new HashMap<>() {{
      //   put("22", new User(88, "knqiufan"));
      // }}));
      // System.out.println(userService.getStringListByList(new ArrayList<>() {{
      //   add("citrus");
      // }}));
      // System.out.println(Arrays.toString(userService.getIdsByUsers(new User[]{new User(32, "oo")})));
      //
      // System.out.println(userService.getMapByList(new ArrayList<>(){{
      //   add(new User(3, "user"));
      //   add(new User(54, "identify"));
      // }}));
      //
      // Order order = orderService.findById(2);
      // System.out.println(order);

    };
  }
}
