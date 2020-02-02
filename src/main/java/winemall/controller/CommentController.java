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
import winemall.bean.Comment;
import winemall.bean.Merchant;
import winemall.bean.Order;
import winemall.bean.Product;
import winemall.dto.Result;
import winemall.service.CommentService;
import winemall.service.OrderService;
import winemall.service.ProductService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Explain: 评论控制器
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/toCommentManage")
    public String toCommentManage() {
        return "commentManage";
    }

    /**
     * @param pn    页码
     * @param size  每页的数据量
     * @param sort  过滤条件
     * @param order 分类条件
     * @Explain 获取历史订单
     */
    @ResponseBody
    @GetMapping("/commentPage")
    public Object commentPage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              @RequestParam(value = "sort", defaultValue = "id") String sort,
                              @RequestParam(value = "order", defaultValue = "desc") String order) {
        PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  size：页大小
        Comment comment = new Comment();
        comment.setParentType("PRODUCT");
        List<Comment> commentList = commentService.queryList(comment);
        commentList.stream().forEach(cl -> {
            Product product = productService.queryDetail(cl.getParentId());
            Order orderi = orderService.queryDetail(cl.getOrderId());
            cl.setProduct(product);
            cl.setOrder(orderi);
        });
        PageInfo pageInfo = new PageInfo(commentList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    @RequestMapping("/toReply")
    public String toReply(Long id, Model model) {
        model.addAttribute("id", id);
        return "reply";
    }

    @ResponseBody
    @RequestMapping("/doReply")
    public Object doReply(Long id, String content, HttpSession session) {
        Merchant merchant = (Merchant) session.getAttribute("LOGIN_USER");
        Comment comment = commentService.queryDetail(id);
        Comment commenti = new Comment();
        commenti.setParentId(comment.getId());
        commenti.setParentType("COMMENT");
        commenti.setOrderId(comment.getOrderId());
        commenti.setUserId(merchant.getId());
        commenti.setContent(content);
        int res = commentService.doAdd(commenti);
        comment.setStatus("Y");
        commentService.doEdit(comment);
        return res > 0 ? Result.success() : Result.error();
    }
}