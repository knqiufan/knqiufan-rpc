package cn.knqiufan.rpc.demo.provider;

import cn.knqiufan.rpc.core.test.TestZkServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

/**
 * 单元测试
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/4 10:59
 */
@SpringBootTest
public class KnrpcDemoProviderApplicationTest {

  static TestZkServer testZkServer = new TestZkServer();

  @BeforeAll
  static void init() throws Exception {
    testZkServer.start();
  }

  @Test
  public void test() {
    System.out.println("===========> KnrpcDemoProviderApplicationTest ....");
  }

  @AfterAll
  static void destroy() throws IOException {
    testZkServer.stop();
  }
}
