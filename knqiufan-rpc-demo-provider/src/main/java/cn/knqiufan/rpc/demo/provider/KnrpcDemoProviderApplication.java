package cn.knqiufan.rpc.demo.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.core.api.RpcRequest;
import cn.knqiufan.rpc.core.api.RpcResponse;
import cn.knqiufan.rpc.core.provider.ProviderBootstrap;
import cn.knqiufan.rpc.core.provider.ProviderConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/8 21:10
 */
@SpringBootApplication
@RestController
@Import({ProviderConfig.class})
public class KnrpcDemoProviderApplication {

  @Autowired
  ProviderBootstrap providerBootstrap;
  public static void main(String[] args) {
    SpringApplication.run(KnrpcDemoProviderApplication.class, args);
  }

  @RequestMapping("/")
  public RpcResponse invoke(@RequestBody RpcRequest request) {
    return providerBootstrap.invoke(request);
  }


  @Bean
  ApplicationRunner providerRun() {
    return x -> {
      RpcRequest rpcRequest = new RpcRequest();
      rpcRequest.setService("cn.knqiufan.rpc.demo.api.UserService");
      rpcRequest.setMethod("findById");
      rpcRequest.setArgs(new Object[]{100});

      RpcResponse invoke = invoke(rpcRequest);
      System.out.println("return: " + invoke.getData());
    };
  }
}
