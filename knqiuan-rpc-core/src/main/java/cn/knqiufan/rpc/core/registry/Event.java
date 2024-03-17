package cn.knqiufan.rpc.core.registry;

import java.util.List;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 21:54
 */
public class Event {
  List<String> data;

  public Event(List<String> data) {
    this.data = data;
  }

  public List<String> getData() {
    return data;
  }

  public void setData(List<String> data) {
    this.data = data;
  }
}
