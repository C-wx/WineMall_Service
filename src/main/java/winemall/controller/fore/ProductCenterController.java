package winemall.controller.fore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import winemall.bean.Comment;
import winemall.bean.Order;
import winemall.bean.OrderLog;
import winemall.bean.Product;
import winemall.dto.Result;
import winemall.service.CommentService;
import winemall.service.OrderLogService;
import winemall.service.OrderService;
import winemall.service.ProductService;

import java.util.List;

/**
 * @Explain: 前台商品控制器
 */
@Controller
public class ProductCenterController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderLogService orderLogService;

    @ResponseBody
    @GetMapping("/getProduct")
    public Object getProduct(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              String name) {
        PageHelper.startPage(pn, size);     //pn:页码  size：页大小
        Product product = new Product();
        product.setStatus("E");
        if (StringUtils.isNotBlank(name)) {
            product.setName(name);
        }
        List<Product> productList = productService.queryList(product);
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
        comments.stream().forEach(cs->{
            Order order = orderService.queryDetail(cs.getOrderId());
            cs.setOrder(order);
            cs.setUserName(order.getReceiveName());
        });
        product.setCommentList(comments);
        return Result.success(product);
    }

    @ResponseBody
    @RequestMapping("/doAddCart")
    public Object doAddCart(OrderLog orderLog) {
        orderLog.setUserId(1L);
        Product product = productService.queryDetail(orderLog.getProductId());
        float price = orderLog.getNum()*product.getPrice();
        orderLog.setPrice(price);
        orderLogService.doAdd(orderLog);
        return Result.success();
    }
}
