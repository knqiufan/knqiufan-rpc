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

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:37
 */
@SpringBootApplication
@Import({ConsumerConfig.class})
public class KnrpcDemoConsumerApplication {

  @KnConsumer
  UserService userService;

  @KnConsumer
  OrderService orderService;

  public static void main(String[] args) {
    SpringApplication.run(KnrpcDemoConsumerApplication.class, args);
  }

  @Bean
  public ApplicationRunner consumerRunner() {
    return x -> {
      User user = userService.findById(1);
      System.out.println(user);
      userService.toString();

      Order order = orderService.findById(2);
      System.out.println(order);

    };
  }
}
