package winemall.controller.fore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import winemall.bean.User;
import winemall.dto.Result;
import winemall.service.UserService;

import java.util.Objects;

/**
 * @Explain: 用户控制器
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @Explain 若用户为第一次登陆，等往数据库插入一条用户记录
     * @param  user 用户传输实体
     * @Return
     */
    @ResponseBody
    @RequestMapping("/doAddUser")
    public Object doAddUser(User user) {
        User useri = userService.queryDetail(user.getOpenId());
        if (Objects.isNull(useri)) {
            userService.doAdd(user);
        }
        return Result.success();
    }

}
