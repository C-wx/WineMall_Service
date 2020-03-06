package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Ser;
import winemall.bean.SerExample;
import winemall.mapper.SerMapper;

import java.util.List;

/**
 * @Explain: 售后服务器
 */
@Service
public class SerService {

    @Autowired
    private SerMapper serMapper;

    public int doAdd(Ser ser) {
        return serMapper.insertSelective(ser);
    }

    public List<Ser> queryList() {
        SerExample serExample = new SerExample();
        serExample.setOrderByClause("id desc");
        return serMapper.selectByExample(serExample);
    }

    public int doEdit(Ser ser) {
        return serMapper.updateByPrimaryKeySelective(ser);
    }

    public Ser queryDetail(Long id) {
        return serMapper.selectByPrimaryKey(id);
    }
}
