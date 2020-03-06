package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.OrderLog;
import winemall.bean.OrderLogExample;
import winemall.mapper.OrderLogMapper;

import java.util.List;

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

    public OrderLog queryLog(String openId, Long productId) {
        OrderLogExample logExample = new OrderLogExample();
        OrderLogExample.Criteria criteria = logExample.createCriteria();
        criteria.andOpenIdEqualTo(openId)
                .andProductIdEqualTo(productId)
                .andStatusNotEqualTo("D")
                .andOrderIdIsNull();          //订单ID 为空 说明是还在购物车中，还未下单
        List<OrderLog> orderLogList = orderLogMapper.selectByExample(logExample);
        return orderLogList.size()>0?orderLogList.get(0):null;
    }

    public int doEdit(OrderLog logi) {
        return orderLogMapper.updateByPrimaryKeySelective(logi);
    }

    public List<OrderLog> queryList(String openId) {
        OrderLogExample orderLogExample = new OrderLogExample();
        OrderLogExample.Criteria criteria = orderLogExample.createCriteria();
        criteria.andOpenIdEqualTo(openId)
                .andOrderIdIsNull()                 //未生成订单ID 说明还未购买
                .andTypeEqualTo("BC")               //表示是加购形式
                .andStatusEqualTo("E");             //表示生效状态
        orderLogExample.setOrderByClause("id DESC");
        return orderLogMapper.selectByExample(orderLogExample);
    }

    public OrderLog queryDetail(Long id) {
        return orderLogMapper.selectByPrimaryKey(id);
    }
}
