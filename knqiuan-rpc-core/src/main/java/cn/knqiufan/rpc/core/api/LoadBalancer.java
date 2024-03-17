package cn.knqiufan.rpc.core.api;

import java.util.List;

/**
 * 负载均衡类 - 随机，轮询，权重轮询、AAWR-自适应
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 16:14
 */
public interface LoadBalancer<T> {
  /**
   * 选择地址
   *
   * @param providers 待选组
   * @return 选定地址
   */
  T choose(List<T> providers);

  /**
   * 负载均衡默认实现
   */
  LoadBalancer DEFAULT = providers -> (providers == null || providers.isEmpty()) ? null : providers.get(0);
}
