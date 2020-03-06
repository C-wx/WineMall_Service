package winemall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Explain: 页面跳转控制器
 */
@Controller
public class PageController {

    /**
     * @Explain 跳转登录页
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * @Explain 跳转首页
     */
    @RequestMapping("/toIndex")
    public String toIndex() {
        return "index";
    }
}
