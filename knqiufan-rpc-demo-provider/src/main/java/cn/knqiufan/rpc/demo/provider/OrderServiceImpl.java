package cn.knqiufan.rpc.demo.provider;

import cn.knqiufan.rpc.core.annotation.KnProvider;
import cn.knqiufan.rpc.demo.api.pojo.Order;
import cn.knqiufan.rpc.demo.api.service.OrderService;
import org.springframework.stereotype.Component;

/**
 * 订单实现类
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/3/9 9:57
 */
@Component
@KnProvider
public class OrderServiceImpl implements OrderService {
  @Override
  public Order findById(Integer id) {
    if(id == 404) {
      throw new RuntimeException("404 Exception");
    }
    return new Order(id.longValue(), 15.6f);
  }
}
