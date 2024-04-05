package cn.knqiufan.rpc.core.test;

import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingCluster;

import java.io.IOException;

/**
 * the test of zookeeper
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/5 14:38
 */
public class TestZkServer {

  TestingCluster cluster;

  public void start() throws Exception {
    InstanceSpec instanceSpec = new InstanceSpec(null, 2182,
            -1, -1, true,
            -1, -1, -1);
    cluster = new TestingCluster(instanceSpec);
    System.out.println("TestingZookeeperServer starting... ...");
    cluster.start();
    cluster.getServers().forEach(s-> System.out.println(s.getInstanceSpec()));
    System.out.println("TestingZookeeperServer started.");
  }

  public void stop() throws IOException {
    System.out.println("TestingZookeeperServer stopping... ...");
    cluster.stop();
    System.out.println("TestingZookeeperServer stopped.");
  }
}
