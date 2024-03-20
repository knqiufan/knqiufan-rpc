package cn.knqiufan.rpc.core.registry;

/**
 * 改变时间监听接口
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 21:48
 */
public interface ChangedListener {
  void fire(Event event);
}
