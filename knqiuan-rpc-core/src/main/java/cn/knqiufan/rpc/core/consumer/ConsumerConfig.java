package cn.knqiufan.rpc.core.consumer;

import cn.knqiufan.rpc.core.api.LoadBalancer;
import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.api.Router;
import cn.knqiufan.rpc.core.cluster.RoundRobinLoadBalancer;
import cn.knqiufan.rpc.core.registry.ZkRegistryCenter;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/10 19:49
 */
@Configuration
public class ConsumerConfig {

  @Value("${knrpc.providers}")
  String providers;
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

  @Bean
  public LoadBalancer loadBalancer() {
    return new RoundRobinLoadBalancer();
  }

  @Bean
  public Router router() {
    return Router.DEFAULT;
  }

  // @Bean(initMethod = "start", destroyMethod = "stop")
  @Bean(initMethod = "start")
  public RegistryCenter registryCenter() {
    return new ZkRegistryCenter();
  }
}
