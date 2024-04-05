package cn.knqiufan.rpc.demo.consumer;

import cn.knqiufan.rpc.core.test.TestZkServer;
import cn.knqiufan.rpc.demo.provider.KnrpcDemoProviderApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/5 15:00
 */
@SpringBootTest(classes = KnrpcDemoConsumerApplicationTest.class)
public class KnrpcDemoConsumerApplicationTest {

  static ApplicationContext applicationContext;

  static TestZkServer testZkServer;

  @BeforeAll
  static void init() throws Exception {
    System.out.println("------------------------------");
    System.out.println("------------------------------");
    System.out.println("------------------------------");
    // 启动一个 zookeeper 测试客户端
    testZkServer.start();
    // 启动 provider
    applicationContext = SpringApplication.run(KnrpcDemoProviderApplication.class,
            "--server.port=8094", "--knrpc.zkServer=111.231.54.184:2182",
            "--logging.level.cn.knqiufan.knrpc=info");
  }

  @Test
  void test() {
    System.out.println("KnrpcDemoConsumerApplicationTest ....");
  }

  @AfterAll
  static void destroy() throws IOException {
    // 停止 provider
    SpringApplication.exit(applicationContext, () -> 0);
    // 关闭 zk
    testZkServer.stop();
  }
}
