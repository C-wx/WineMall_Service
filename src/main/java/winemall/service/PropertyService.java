package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Property;
import winemall.bean.PropertyExample;
import winemall.mapper.PropertyMapper;

import java.util.List;

/**
 * @Explain: 商品属性处理器
 */
@Service
public class PropertyService {

    @Autowired
    private PropertyMapper propertyMapper;

    public int doAdd(Property property) {
        return propertyMapper.insertSelective(property);
    }

    public List<Property> queryList(Long id) {
        PropertyExample propertyExample = new PropertyExample();
        propertyExample.createCriteria().andProductIdEqualTo(id).andStatusNotEqualTo("D");
        propertyExample.setOrderByClause("id desc");
        return propertyMapper.selectByExample(propertyExample);
    }
}