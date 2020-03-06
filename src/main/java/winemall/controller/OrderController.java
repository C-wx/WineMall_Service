package winemall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import winemall.bean.Order;
import winemall.bean.Product;
import winemall.dto.Result;
import winemall.service.OrderService;
import winemall.service.ProductService;

import java.util.Date;
import java.util.List;

/**
 * @Explain: 订单控制器
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/toOrderManage")
    public String toOrderManage() {
        return "orderManage";
    }

    /**
     * @param pn    页码
     * @param size  每页的数据量
     * @param sort  过滤条件
     * @param order 分类条件
     * @Explain 获取历史订单
     */
    @ResponseBody
    @GetMapping("/orderPage")
    public Object orderPage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                            @RequestParam(value = "sort", defaultValue = "id") String sort,
                            @RequestParam(value = "order", defaultValue = "desc") String order,
                            @RequestParam(value = "orderCode", defaultValue = "%") String orderCode) {
        PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  size：页大小
        List<Order> orderList = orderService.queryList(orderCode);
        PageInfo pageInfo = new PageInfo(orderList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * @param orderCode 订单编码
     * @Explain 获取订单编码所对应的订单
     */
    @RequestMapping("/toOrderDetail")
    public String toOrderDetail(String orderCode, Model model) {
        List<Order> orderList = orderService.getOrders(orderCode);
        for (Order order : orderList) {
            Product product = productService.queryDetail(order.getProductId());
            order.setProduct(product);
        }
        model.addAttribute("orderList", orderList);
        return "orderDetail";
    }

    /**
     * @param orderCode 订单编码
     * @Explain 跳转发货页面
     */
    @RequestMapping("/toOrderDelivery")
    public String toOrderDelivery(String orderCode, Model model) {
        model.addAttribute("orderCode", orderCode);
        return "orderDelivery";
    }

    /**
     * @param order 订单传输实体
     * @Explain 操作订单
     */
    @ResponseBody
    @RequestMapping("/doOpeOrder")
    public Object doOpeOrder(Order order) {
        if ("WD".equals(order.getStatus())) {   //WD 为付款操作
            order.setPayTime(new Date());       // 设置付款时间
            List<Order> orderList = orderService.getOrders(order.getOrderCode());   //获取当前订单编码下的所有订单
            for (Order o : orderList) {     //遍历订单
                Product product = productService.queryDetail(o.getProductId());     //获取对应商品
                product.setSaled(product.getSaled() + o.getNum());      //改变商品销量
                product.setStock(product.getStock() - o.getNum());      //改变商品库存
                productService.doEdit(product);
            }
        }
        orderService.doOpeOrder(order);
        return Result.success();
    }

    /**
     * @param order 订单传输实体
     * @Explain 发货操作
     */
    @ResponseBody
    @RequestMapping("/doOrderDelivery")
    public Object doOrderDelivery(Order order) {
        order.setDeliveryTime(new Date());
        order.setStatus("WC");
        orderService.doOpeOrder(order);
        return Result.success();
    }
}
