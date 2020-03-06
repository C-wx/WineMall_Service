package winemall.controller.fore;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    /**
     * @param pn       页码
     * @param size     页的大小
     * @param name     商品名称
     * @param isActive 是否参加活动
     * @Explain 获取商品列表
     * @Return
     */
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

    /**
     * @param id 商品ID
     * @Explain 获取商品详情
     */
    @ResponseBody
    @RequestMapping("/getProductDetail")
    public Object getProductDetail(Long id) {
        Product product = productService.queryDetail(id);
        Comment comment = new Comment();
        comment.setParentId(product.getId());
        List<Comment> comments = commentService.queryList(comment);     //获取该商品下的评论
        comments.stream().forEach(cs -> {
            Order order = orderService.getCorrespond(cs.getParentId(), cs.getOrderCode());
            if (order != null) {        //获取评论所对应的的订单信息
                cs.setOrder(order);
                cs.setUserName(order.getReceiveName());
            }
        });
        List<Image> images = imageService.queryList(id);        //获取商品图片
        product.setImageList(images);
        List<Property> propertyList = propertyService.queryList(product.getId());       //获取商品属性
        product.setPropertyList(propertyList);
        product.setCommentList(comments);
        return Result.success(product);
    }

    /**
     * @param orderLog 购物车传输实体
     * @Explain 添加购物车
     */
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

    /**
     * @param openId 用户标识ID
     * @Explain 获取购物车列表
     */
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

    /**
     * @param orderLog 购物车传输实体
     * @Explain 生成购物车
     */
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

    /**
     * @param orderList 订单集合
     * @param addrId    地址ID
     * @param remark    备注
     * @Explain 生成订单
     * @Return
     */
    @ResponseBody
    @RequestMapping("/saveOrder")
    public Object saveOrder(String orderList, Long addrId, String remark) {
        JSONArray orderArray = JSONArray.parseArray(orderList);
        Addr addr = addrService.queryDetail(addrId);
        String orderCode = UUID.randomUUID().toString().substring(0, 8);        //随机生成订单编码
        for (Object o : orderArray) {                                           //拆单操作
            Order oi = JSON.toJavaObject((JSON) o, Order.class);
            oi.setOrderCode(orderCode);
            oi.setAddr(addr.getAddr());
            oi.setPostCode(addr.getPostCode());
            oi.setReceiveName(addr.getName());
            oi.setPhone(addr.getPhone());
            oi.setComment(remark);
            orderService.doAdd(oi);                                //生成订单
            if (StringUtils.isNotBlank(oi.getCartId())) {       //说明是从购物车中下单
                OrderLog orderLog = new OrderLog();
                orderLog.setId(Long.valueOf(oi.getCartId()));
                orderLog.setOrderId(oi.getId());
                orderLogService.doEdit(orderLog);
            }
        }
        return Result.success(orderCode);               //将商品订单编码返回
    }

    /**
     * @param status 订单状态
     * @Explain 根据订单状态获取对应订单
     */
    @ResponseBody
    @RequestMapping("/getOrderList")
    public Object getOrderList(String status) {
        if ("ALL".equals(status)) {
            status = "%";
        } else {
            status = status + "%";
        }
        List<List<Order>> orderList = orderService.getOrderList(status);
        return Result.success(orderList);
    }

    /**
     * @param id 订单ID
     * @Explain 获取订单详情
     * @Return
     */
    @ResponseBody
    @RequestMapping("/getOrderDetail")
    public Object getOrderDetail(Long id) {
        Order order = orderService.queryDetail(id);
        return Result.success(order);
    }

    /**
     * @param comment 评论传输实体
     * @Explain 发布评论
     */
    @ResponseBody
    @RequestMapping("/doComment")
    public Object doComment(Comment comment) {
        List<Order> orderList = orderService.getOrders(comment.getOrderCode());     //获取订单编号对应的订单
        for (Order order : orderList) {
            comment.setParentId(order.getProductId());      //拆分订单  如果一个订单中由两个商品，那么这条评论应该在两个商品中都要插入
            commentService.doAdd(comment);
        }
        Order order = new Order();
        order.setOrderCode(comment.getOrderCode());
        order.setStatus("YR");          //改变订单状态       “YR”：已评论状态
        orderService.doOpeOrder(order);
        return Result.success();
    }

    /**
     * @param ser 售后服务传输实体
     * @Explain 发起售后
     */
    @ResponseBody
    @RequestMapping("/doService")
    public Object doService(Ser ser) {
        serService.doAdd(ser);
        Order order = new Order();
        order.setOrderCode(ser.getOrderCode());
        order.setStatus("DW");
        order.setServiceId(ser.getId());
        orderService.doOpeOrder(order);
        return Result.success();
    }
}