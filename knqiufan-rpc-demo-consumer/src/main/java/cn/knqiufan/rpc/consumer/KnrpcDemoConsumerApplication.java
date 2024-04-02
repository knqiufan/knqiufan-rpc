package cn.knqiufan.rpc.consumer;

import cn.knqiufan.rpc.core.annotation.KnConsumer;
import cn.knqiufan.rpc.core.consumer.ConsumerConfig;
import cn.knqiufan.rpc.demo.api.pojo.User;
import cn.knqiufan.rpc.demo.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ApplicationRunner consumerRunner() {
    return x -> {
      User user = userService.findById(1);
      log.info(user.toString());
      // log.info(userService.getByUser(new User(1, "knqiufan")));
      // log.info(userService.getByLongId(1));
      //
      //
      // User maidou = userService.findById(2, "maidou");
      // log.info(maidou);
      // log.info(userService.getById(22));
      // log.info(userService.toString());
      // log.info(userService.getDouble(33));
      // log.info(userService.isTrue(4));
      // log.info(userService.getList());
      // log.info(Arrays.toString(userService.getIds()));
      // log.info(Arrays.toString(userService.getLongIds()));
      // log.info(Arrays.toString(userService.getIdsByIds(new int[]{99, 55})));
      //
      // log.info(userService.getMap());
      // log.info(userService.getListByMap(new HashMap<>() {{
      //   put("2", 4);
      // }}));
      // log.info(userService.getStringListByMap(new HashMap<>() {{
      //   put("22", new User(88, "knqiufan"));
      // }}));
      // log.info(userService.getMapListByMap(new HashMap<>() {{
      //   put("22", new User(88, "knqiufan"));
      // }}));
      // log.info(userService.getStringListByList(new ArrayList<>() {{
      //   add("citrus");
      // }}));
      // log.info(Arrays.toString(userService.getIdsByUsers(new User[]{new User(32, "oo")})));
      //
      // log.info(userService.getMapByList(new ArrayList<>(){{
      //   add(new User(3, "user"));
      //   add(new User(54, "identify"));
      // }}));
      //
      // Order order = orderService.findById(2);
      // log.info(order);

    };
  }
}
