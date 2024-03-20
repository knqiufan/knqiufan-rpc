package cn.knqiufan.rpc.core.registry;

import cn.knqiufan.rpc.core.meta.InstanceMeta;

import java.util.List;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/17 21:54
 */
public class Event {
  List<InstanceMeta> data;

  public Event(List<InstanceMeta> data) {
    this.data = data;
  }

  public List<InstanceMeta> getData() {
    return data;
  }

  public void setData(List<InstanceMeta> data) {
    this.data = data;
  }
}
