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
import winemall.bean.Ser;
import winemall.dto.Result;
import winemall.service.OrderService;
import winemall.service.ProductService;
import winemall.service.SerService;

import java.util.List;

/**
 * @Explain: 售后控制器
 */
@Controller
public class ServiceController {

    @Autowired
    private SerService serService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/toServiceManage")
    public String toServiceManage() {
        return "serviceManage";
    }

    /**
     * @param pn    页码
     * @param size  每页的数据量
     * @param sort  过滤条件
     * @param order 分类条件
     * @Explain 获取历史订单
     */
    @ResponseBody
    @GetMapping("/servicePage")
    public Object servicePage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              @RequestParam(value = "sort", defaultValue = "id") String sort,
                              @RequestParam(value = "order", defaultValue = "desc") String order) {
        PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  size：页大小
        List<Ser> serList = serService.queryList();
        for (Ser ser : serList) {
            Order oi = orderService.queryDetail(ser.getOrderId());
            Product product = productService.queryDetail(oi.getProductId());
            ser.setOrder(oi);
            ser.setProduct(product);
        }
        PageInfo pageInfo = new PageInfo(serList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    @RequestMapping("/toRefuse")
    public String toRefuse(Long id, Long oid, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("oid", oid);
        return "refuse";
    }

    @ResponseBody
    @RequestMapping("/doOpeService")
    public Object doOpeService(Ser ser) {
        serService.doEdit(ser);
        Order order = new Order();
        order.setId(ser.getOrderId());
        if (!StringUtils.isNotBlank(ser.getTrackCode())) {
            if ("F".equals(ser.getStatus())) {
                order.setStatus("DF");
            } else {
                order.setStatus("DS");
            }
        }else{
            order.setStatus("DWTK");
        }
        orderService.doEdit(order);
        return Result.success();
    }

    @ResponseBody
    @RequestMapping("/getReason")
    public Object getReason(Long serviceId) {
        Ser si = serService.queryDetail(serviceId);
        return Result.success(si);
    }
}
