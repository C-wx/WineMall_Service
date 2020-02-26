package winemall.controller.fore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import winemall.bean.*;
import winemall.dto.Result;
import winemall.service.*;
import winemall.utils.RandomUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Explain: 前台商品/订单/评论控制器
 */
@Slf4j
@Controller
public class ProductCenterController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AddrService addrService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SerService serService;

    @ResponseBody
    @GetMapping("/getProduct")
    public Object getProduct(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             String name, String isActive) {
        PageHelper.startPage(pn, size);     //pn:页码  size：页大小
        Product product = new Product();
        product.setStatus("E");
        if (StringUtils.isNotBlank(name)) {
            product.setName(name);
        }
        if (StringUtils.isNotBlank(isActive)) {
            product.setIsActive(isActive);
        }
        List<Product> productList = productService.queryList(product);
        for (Product pi : productList) {
            List<Image> images = imageService.queryList(pi.getId());
            pi.setImageList(images);
        }
        PageInfo pageInfo = new PageInfo(productList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    @ResponseBody
    @RequestMapping("/getProductDetail")
    public Object getProductDetail(Long id) {
        Product product = productService.queryDetail(id);
        Comment comment = new Comment();
        comment.setParentId(product.getId());
        comment.setParentType("PRODUCT");
        List<Comment> comments = commentService.queryList(comment);
        comments.stream().forEach(cs -> {
            Order order = orderService.queryDetail(cs.getOrderId());
            cs.setOrder(order);
            cs.setUserName(order.getReceiveName());
        });
        List<Image> images = imageService.queryList(id);
        product.setImageList(images);
        List<Property> propertyList = propertyService.queryList(product.getId());
        product.setPropertyList(propertyList);
        product.setCommentList(comments);
        return Result.success(product);
    }

    @ResponseBody
    @RequestMapping("/doAddCart")
    public Object doAddCart(OrderLog orderLog) {
        int res;
        Product product = productService.queryDetail(orderLog.getProductId());
        float price = orderLog.getNum() * product.getPrice();
        orderLog.setPrice(price);
        /**
         * 先从购物清单中查找此用户的购物车中是否已经加购过此商品，如果已经加购过，那便数量和价格相加，反之添加一条新的购物车记录
         */
        OrderLog logi = orderLogService.queryLog(orderLog.getOpenId(), orderLog.getProductId());
        if (Objects.isNull(logi)) {             //为空说明之前未加购过此商品，生成一条新的购物车记录
            res = orderLogService.doAdd(orderLog);
        } else {                                  //否则之前加购过此商品
            logi.setNum(logi.getNum() + orderLog.getNum());
            logi.setPrice(logi.getPrice() + price);
            res = orderLogService.doEdit(logi);
        }
        return res > 0 ? Result.success() : Result.error();
    }

    @ResponseBody
    @RequestMapping("/getCarList")
    public Object getCarList(String openId) {
        List<OrderLog> orderLogs = orderLogService.queryList(openId);
        orderLogs.stream().forEach(orderLog -> {
            Product product = productService.queryDetail(orderLog.getProductId());
            List<Image> images = imageService.queryList(product.getId());
            product.setImageList(images);
            orderLog.setProduct(product);
            orderLog.setSelected(false);
        });
        return Result.success(orderLogs);
    }

    @ResponseBody
    @RequestMapping("/doOpeOrderLog")
    public Object doOpeOrderLOg(OrderLog orderLog) {
        int res;
        if (StringUtils.isNotBlank(orderLog.getStatus())) {     //status 不为空的时候为删除操作
            res = orderLogService.doEdit(orderLog);
        } else {                                                 //否则是更新操作
            OrderLog ol = orderLogService.queryDetail(orderLog.getId());
            Product product = productService.queryDetail(ol.getProductId());
            orderLog.setPrice(orderLog.getNum() * product.getPrice());
            res = orderLogService.doEdit(orderLog);
        }
        return res > 0 ? Result.success() : Result.error();
    }

    @ResponseBody
    @RequestMapping("/saveOrder")
    public Object saveOrder(String orderList,
                            String openId,
                            String addrId,
                            Integer num,
                            Float price,
                            String content,
                            Boolean byCart) {
        List<Long> orderids = new ArrayList<>();
        String[] orderLogIds = orderList.substring(0, orderList.length() - 1).split(",");
        Addr addr = addrService.queryDetail(Long.valueOf(addrId));
        Arrays.stream(orderLogIds).forEach(ol -> {
            Order order = new Order();
            order.setStatus("WP");
            order.setOpenId(openId);
            order.setAddr(addr.getAddr());
            order.setPostCode(addr.getPostCode());
            order.setReceiveName(addr.getName());
            order.setPhone(addr.getPhone());
            order.setComment(content);
            order.setOrderCode(RandomUtil.getUUID());
            if (byCart) {
                OrderLog orderLog = orderLogService.queryDetail(Long.valueOf(ol));
                order.setProductId(orderLog.getProductId());
                order.setNum(orderLog.getNum());
                order.setPrice(orderLog.getPrice());
                orderService.doAdd(order);
                orderLog.setOrderId(order.getId());
                orderLogService.doEdit(orderLog);
                orderids.add(order.getId());
            } else {
                order.setProductId(Long.valueOf(ol));
                order.setNum(num);
                order.setPrice(price);
                orderService.doAdd(order);
                orderids.add(order.getId());
            }
        });
        return Result.success(orderids);
    }

    @ResponseBody
    @RequestMapping("/getOrderList")
    public Object getOrderList(String status) {
        Order order = new Order();
        if (!"ALL".equals(status)) {
            order.setStatus(status);
        }
        List<Order> orderList = orderService.queryList(order);
        orderList.stream().forEach(ol -> {
            Product product = productService.queryDetail(ol.getProductId());
            List<Image> images = imageService.queryList(product.getId());
            product.setImageList(images);
            ol.setProduct(product);
        });
        return Result.success(orderList);
    }

    @ResponseBody
    @RequestMapping("/getOrderDetail")
    public Object getOrderDetail(Long id) {
        Order order = orderService.queryDetail(id);
        return Result.success(order);
    }

    @ResponseBody
    @RequestMapping("/doComment")
    public Object doComment(Comment comment) {
        int res = commentService.doAdd(comment);
        Order order = new Order();
        order.setId(comment.getOrderId());
        order.setStatus("YR");
        orderService.doEdit(order);
        return res > 0 ? Result.success() : Result.error();
    }

    @ResponseBody
    @RequestMapping("/doService")
    public Object doService(Ser ser) {
        serService.doAdd(ser);
        Order order = new Order();
        order.setId(ser.getOrderId());
        order.setStatus("DW");
        order.setServiceId(ser.getId());
        orderService.doEdit(order);
        return Result.success();
    }
}