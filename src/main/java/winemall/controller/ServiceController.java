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
import winemall.bean.Ser;
import winemall.dto.Result;
import winemall.service.OrderService;
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
        PageInfo pageInfo = new PageInfo(serList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * @param id        售后ID
     * @param orderCode 订单编码
     * @Explain 跳转拒绝页面
     */
    @RequestMapping("/toRefuse")
    public String toRefuse(Long id, String orderCode, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("orderCode", orderCode);
        return "refuse";
    }

    /**
     * @param ser 售后传输实体
     * @Explain 处理售后
     * @Return
     */
    @ResponseBody
    @RequestMapping("/doOpeService")
    public Object doOpeService(Ser ser) {
        serService.doEdit(ser);
        Order order = new Order();
        order.setOrderCode(ser.getOrderCode());
        if (!StringUtils.isNotBlank(ser.getTrackCode())) {
            if ("F".equals(ser.getStatus())) {      //F：拒绝
                order.setStatus("DF");              //更新订单状态    DF: 拒绝退款      DS: 同意退款
            } else if ("S".equals(ser.getStatus())) {
                order.setStatus("DS");
            } else if ("SS".equals(ser.getStatus())) {
                order.setStatus("DYTK");
            }
        } else {
            order.setStatus("DWTK");                //同意退款，等待商家退款
        }
        orderService.doOpeOrder(order);
        return Result.success();
    }

    /**
     * @param serviceId 售后ID
     * @Explain 查看拒绝原因
     * @Return
     */
    @ResponseBody
    @RequestMapping("/getReason")
    public Object getReason(Long serviceId) {
        Ser si = serService.queryDetail(serviceId);
        return Result.success(si);
    }
}
