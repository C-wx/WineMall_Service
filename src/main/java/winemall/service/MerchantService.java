package winemall.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Merchant;
import winemall.bean.MerchantExample;
import winemall.dto.UserDto;
import winemall.mapper.MerchantMapper;

import java.util.List;

/**
 * @Explain: 登录处理器
 */
@Service
public class MerchantService {

    @Autowired
    private MerchantMapper merchantMapper;

    public Merchant getOne(UserDto user) {
        MerchantExample merchantExample = new MerchantExample();
        MerchantExample.Criteria criteria = merchantExample.createCriteria();
        if (StringUtils.isNotBlank(user.getAccount())) {
            criteria.andAccountEqualTo(user.getAccount());
        }
        if (StringUtils.isNotBlank(user.getPwd())) {
            criteria.andPwdEqualTo(user.getPwd());
        }
        List<Merchant> merchantList = merchantMapper.selectByExample(merchantExample);
        return merchantList.size()>0?merchantList.get(0):null;
    }
}
