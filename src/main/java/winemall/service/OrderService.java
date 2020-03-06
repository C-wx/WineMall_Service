package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Image;
import winemall.bean.Order;
import winemall.bean.OrderExample;
import winemall.bean.Product;
import winemall.mapper.OrderMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @Explain: 订单处理器
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    public List<Order> queryList(String orderCode) {
        orderCode = "%"+orderCode+"%";
        return orderMapper.getALlOrderByOrderCode(orderCode);
    }

    public Order queryDetail(Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    public int doEdit(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    public int doAdd(Order order) {
        return orderMapper.insertSelective(order);
    }

    public void doOpeOrder(Order order) {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderCodeEqualTo(order.getOrderCode());
        orderMapper.updateByExampleSelective(order, orderExample);
    }

    public List<List<Order>> getOrderList(String order) {
        List<String> orderCodeList = orderMapper.getOrderCodes(order);
        List<List<Order>> totalOrderList = new ArrayList<>();
        for (String orderCode : orderCodeList) {
            List<Order> orderList = getOrders(orderCode);
            orderList.stream().forEach(ol -> {
                Product product = productService.queryDetail(ol.getProductId());
                List<Image> images = imageService.queryList(product.getId());
                product.setImageList(images);
                ol.setProduct(product);
            });
            totalOrderList.add(orderList);
        }
        return totalOrderList;
    }

    public List<Order> getOrders(String orderCode) {
        OrderExample example = new OrderExample();
        example.createCriteria().andOrderCodeEqualTo(orderCode);
        return orderMapper.selectByExample(example);
    }

    public Order getCorrespond(Long parentId, String orderCode) {
        OrderExample example = new OrderExample();
        example.createCriteria().andProductIdEqualTo(parentId).andOrderCodeEqualTo(orderCode);
        List<Order> orderList = orderMapper.selectByExample(example);
        return orderList.size()>0?orderList.get(0):null;
    }
}
