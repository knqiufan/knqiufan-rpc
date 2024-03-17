package cn.knqiufan.rpc.core.registry;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 21:48
 */
public interface ChangedListener {
  void fire(Event event);
}
