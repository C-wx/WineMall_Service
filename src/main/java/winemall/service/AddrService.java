package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Addr;
import winemall.bean.AddrExample;
import winemall.mapper.AddrMapper;

import java.util.List;

/**
 * @Explain: 地址处理器
 */
@Service
public class AddrService {

    @Autowired
    private AddrMapper addrMapper;

    public List<Addr> queryList(String openId) {
        AddrExample addrExample = new AddrExample();
        addrExample.createCriteria().andOpenIdEqualTo(openId).andStatusNotEqualTo("D");
        addrExample.setOrderByClause("id desc");
        return addrMapper.selectByExample(addrExample);
    }

    public int doAdd(Addr addr) {
        return addrMapper.insertSelective(addr);
    }

    public Addr queryDetail(Long id) {
        return addrMapper.selectByPrimaryKey(id);
    }

    public int doEdit(Addr addr) {
        return addrMapper.updateByPrimaryKeySelective(addr);
    }
}
