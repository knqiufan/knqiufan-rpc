package cn.knqiufan.rpc.core.provider;

import cn.knqiufan.rpc.core.api.RegistryCenter;
import cn.knqiufan.rpc.core.consumer.ConsumerBootstrap;
import cn.knqiufan.rpc.core.registry.ZkRegistryCenter;
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
 * @date 2024/3/9 1:28
 */
@Configuration
public class ProviderConfig {

  @Bean
  ProviderBootstrap creatProviderBootstrap() {
    return new ProviderBootstrap();
  }

  @Bean
  ProviderInvoker providerInvoker(@Autowired ProviderBootstrap providerBootstrap) {
    return new ProviderInvoker(providerBootstrap);
  }

  @Bean
  @Order(Integer.MAX_VALUE)
  public ApplicationRunner providerBootstrapRunner(@Autowired ProviderBootstrap providerBootstrap) {
    return x -> {
      providerBootstrap.start();
    };
  }

  @Bean
  public RegistryCenter registryCenter() {
    return new ZkRegistryCenter();
  }


}
