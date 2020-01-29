package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.OrderLog;
import winemall.mapper.OrderLogMapper;

/**
 * @Explain: 订单日志处理器
 */
@Service
public class OrderLogService {

    @Autowired
    private OrderLogMapper orderLogMapper;

    public int doAdd(OrderLog orderLog) {
        return orderLogMapper.insertSelective(orderLog);
    }
}
