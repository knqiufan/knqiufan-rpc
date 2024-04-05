package cn.knqiufan.rpc.consumer;

import cn.knqiufan.rpc.core.annotation.KnConsumer;
import cn.knqiufan.rpc.core.consumer.ConsumerConfig;
import cn.knqiufan.rpc.demo.api.pojo.User;
import cn.knqiufan.rpc.demo.api.service.UserService;
import org.junit.jupiter.api.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private static final Logger log = LoggerFactory.getLogger(KnrpcDemoConsumerApplication.class);

  @KnConsumer
  UserService userService;

  // @KnConsumer
  // OrderService orderService;

  @RequestMapping("/")
  public User findById(int id) {
    return userService.findById(id);
  }

  public static void main(String[] args) {
    SpringApplication.run(KnrpcDemoConsumerApplication.class, args);
  }

  @Bean
  @Order(value = Integer.MAX_VALUE)
  public ApplicationRunner consumerRunner() {
    return x -> {
      log.info("case1 ===> findById(int id): {}", userService.findById(1));
      log.info("case2 ===> getByUser: {}", userService.getByUser(new User(1, "knqiufan")));
      log.info("case3 ===> getByLongId: {}", userService.getByLongId(1));
      log.info("case4 ===> findById(int id, String name): {}", userService.findById(2, "maidou"));
      log.info("case5 ===> userService.getById: {}", userService.getById(22));
      log.info("case6 ===> userService.toString(): {}", userService.toString());
      log.info("case7 ===> userService.getDouble: {}", userService.getDouble(33));
      log.info("case8 ===> userService.isTrue: {}", userService.isTrue(4));
      log.info("case9 ===> userService.getList(): {}", userService.getList());
      log.info("case10 ===> userService.getIds(): {}", Arrays.toString(userService.getIds()));
      log.info("case11 ===> userService.getLongIds(): {}", Arrays.toString(userService.getLongIds()));
      log.info("case12 ===> userService.getIdsByIds(int[] i)： {}", Arrays.toString(userService.getIdsByIds(new int[]{99, 55})));
      log.info("case13 ===> userService.getMap(): {}", userService.getMap());
      log.info("case14 ===> userService.getListByMap： {}", userService.getListByMap(new HashMap<>() {{
        put("2", 4);
      }}));
      log.info("case15 ===> userService.getStringListByMap: {}", userService.getStringListByMap(new HashMap<>() {{
        put("22", new User(88, "knqiufan"));
      }}));
      log.info("case16 ===> userService.getMapListByMap: {}", userService.getMapListByMap(new HashMap<>() {{
        put("22", new User(88, "knqiufan"));
      }}));
      log.info("case17 ===> userService.getStringListByList: {}", userService.getStringListByList(new ArrayList<>() {{
        add("citrus");
      }}));
      log.info("case18 ===> userService.getIdsByUsers: {}", Arrays.toString(userService.getIdsByUsers(new User[]{new User(32, "oo")})));

      log.info("case19 ===> userService.getMapByList: {}", userService.getMapByList(new ArrayList<>(){{
        add(new User(3, "user"));
        add(new User(54, "identify"));
      }}));
      log.info("case20 ===> userService.ex: {}", userService.ex(true));

    };
  }
}
