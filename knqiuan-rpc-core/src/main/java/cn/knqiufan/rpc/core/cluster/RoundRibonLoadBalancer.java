package cn.knqiufan.rpc.core.cluster;

import cn.knqiufan.rpc.core.api.LoadBalancer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡策略 - 轮询
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 17:14
 */
public class RoundRibonLoadBalancer<T> implements LoadBalancer<T> {

  AtomicInteger index = new AtomicInteger();

  @Override
  public T choose(List<T> providers) {
    if (providers == null || providers.isEmpty()) {
      return null;
    }
    if (providers.size() == 1) {
      return providers.get(0);
    }
    // 缺点：会有上限
    return providers.get((index.getAndIncrement() & 0x7fffffff) % providers.size());
  }
}
