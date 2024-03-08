package cn.knqiufan.rpc.core.provider;

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
  ProviderBootstrap providerBootstrap() {
    return new ProviderBootstrap();
  }
}
