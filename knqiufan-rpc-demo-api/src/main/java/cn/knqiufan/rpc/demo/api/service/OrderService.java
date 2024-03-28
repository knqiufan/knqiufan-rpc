package cn.knqiufan.rpc.demo.api.service;

import cn.knqiufan.rpc.demo.api.pojo.Order;

/**
 * 类描述
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/9 9:56
 */
public interface OrderService {
  Order findById(Integer id);
}
