package winemall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import winemall.bean.Merchant;
import winemall.dto.Result;
import winemall.dto.UserDto;
import winemall.service.MerchantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @Explain: 登录控制器
 */
@Controller
public class LoginController {

    @Autowired
    private MerchantService merchantService;

    /**
     * @Explain 登录操作
     * @param  user 登陆用户传输实体
     * @Return
     */
    @ResponseBody
    @RequestMapping("/doLogin")
    public Object doLogin(UserDto user, HttpSession session) {
        Merchant merchant = merchantService.getOne(user);
        if (!session.getAttribute("img_session_code").toString().equalsIgnoreCase(user.getVerifyCode())) {
            return Result.error(511, "验证码不正确,请重新输入");
        } else if (Objects.isNull(merchant)) {
            return Result.error(512, "用户名或密码错误");
        } else {
            session.setAttribute("LOGIN_USER",merchant);
            return Result.success();
        }
    }

    /**
     * @Explain 退出登录
     */
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        request.getSession().removeAttribute("LOGIN_USER");
        request.getSession().invalidate();
        return new ModelAndView(new RedirectView("/toLogin"));
    }
}
