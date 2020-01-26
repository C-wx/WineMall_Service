package winemall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Explain: 页面跳转控制器
 * @Date: 2020/1/25
 */
@Controller
public class PageController {

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }
}
