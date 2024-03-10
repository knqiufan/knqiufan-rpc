package cn.knqiufan.rpc.core.consumer;

import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:49
 */
@Configuration
public class ConsumerConfig {

  @Bean
  ConsumerBootstrap createConsumerBootstrap() {
    return new ConsumerBootstrap();
  }

  @Bean
  @Order(Integer.MAX_VALUE)
  public ApplicationRunner consumerBootstrapRunner(@Autowired ConsumerBootstrap consumerBootstrap) {
    return x -> {
      consumerBootstrap.start();
    };
  }
}
