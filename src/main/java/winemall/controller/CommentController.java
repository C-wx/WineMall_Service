package winemall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import winemall.bean.Comment;
import winemall.bean.Product;
import winemall.dto.Result;
import winemall.service.CommentService;
import winemall.service.ProductService;

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
        List<Comment> commentList = commentService.queryList(comment);
        commentList.stream().forEach(cl -> {
            Product product = productService.queryDetail(cl.getParentId());
            cl.setProduct(product);
        });
        PageInfo pageInfo = new PageInfo(commentList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * @param comment 评论传输实体
     * @Explain 删除评论
     * @Return
     */
    @ResponseBody
    @RequestMapping("/doOpeComment")
    public Object doOpeComment(Comment comment) {
        commentService.doEdit(comment);
        return Result.success();
    }
}