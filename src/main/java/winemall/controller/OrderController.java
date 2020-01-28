package winemall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
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
                            String orderCode) {
        PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  size：页大小
        Order o = new Order();
        if (StringUtils.isNotBlank(orderCode)) {
            o.setOrderCode(orderCode);
        }
        List<Order> orderList = orderService.queryList(o);
        orderList.stream().forEach(ol -> {
            Product product = productService.queryDetail(ol.getProductId());
            ol.setProduct(product);
        });
        PageInfo pageInfo = new PageInfo(orderList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    @RequestMapping("/toOrderDetail")
    public String toOrderDetail(Long id, Model model) {
        Order order = orderService.queryDetail(id);
        Product product = productService.queryDetail(order.getProductId());
        order.setProduct(product);
        model.addAttribute("order", order);
        return "orderDetail";
    }

    @RequestMapping("/toOrderDelivery")
    public String toOrderDelivery(Long id, Model model) {
        Order order = orderService.queryDetail(id);
        Product product = productService.queryDetail(order.getProductId());
        order.setProduct(product);
        model.addAttribute("order", order);
        return "orderDelivery";
    }

    @ResponseBody
    @RequestMapping("/doOpeOrder")
    public Object doOpeOrder(Order order) {
        int res = orderService.doEdit(order);
        return res > 0 ? Result.success() : Result.error();
    }

    @ResponseBody
    @RequestMapping("/doOrderDelivery")
    public Object doOrderDelivery(Order order) {
        order.setDeliveryTime(new Date());
        order.setStatus("WC");
        int res = orderService.doEdit(order);
        return res > 0 ? Result.success() : Result.error();
    }
}
