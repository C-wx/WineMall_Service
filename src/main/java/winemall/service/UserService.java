package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.User;
import winemall.bean.UserExample;
import winemall.mapper.UserMapper;

import java.util.List;

/**
 * @Explain: 用户处理器
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User queryDetail(String openId) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andOpenIdEqualTo(openId);
        List<User> userList = userMapper.selectByExample(userExample);
        return userList.size() > 0 ? userList.get(0) : null;
    }

    public int doAdd(User useri) {
        return userMapper.insertSelective(useri);
    }
}
