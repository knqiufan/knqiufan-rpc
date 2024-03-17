package cn.knqiufan.rpc.core.api;

import java.util.List;

/**
 * 路由
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 16:14
 */
public interface Router<T> {
  /**
   * 从一级地址中选到二级地址
   *
   * @param providers 待选组
   * @return 选定地址
   */
  List<T> route(List<T> providers);

  /**
   * 默认实现
   */
  Router DEFAULT = r -> r;
}
