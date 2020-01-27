package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Order;
import winemall.bean.OrderExample;
import winemall.mapper.OrderMapper;

import java.util.List;

/**
 * @Explain: 订单处理器
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    public List<Order> queryList(Order o) {
        OrderExample orderExample = new OrderExample();
        return orderMapper.selectByExample(orderExample);
    }

    public Order queryDetail(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    public int doEdit(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }
}
