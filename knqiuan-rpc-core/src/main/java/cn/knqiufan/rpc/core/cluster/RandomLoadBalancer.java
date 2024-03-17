package cn.knqiufan.rpc.core.cluster;

import cn.knqiufan.rpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡策略 - 随机
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 17:14
 */
public class RandomLoadBalancer<T> implements LoadBalancer<T> {

  Random random = new Random();

  @Override
  public T choose(List<T> providers) {
    if (providers == null || providers.isEmpty()) {
      return null;
    }
    if (providers.size() == 1) {
      return providers.get(0);
    }
    return providers.get(random.nextInt(providers.size()));
  }
}
