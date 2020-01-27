package winemall.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Product;
import winemall.bean.ProductExample;
import winemall.mapper.ProductMapper;

import java.util.List;

/**
 * @Explain: 产品处理器
 */
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public List<Product> queryList(Product product) {
        ProductExample productExample = new ProductExample();
        ProductExample.Criteria criteria = productExample.createCriteria();
        if (StringUtils.isNotBlank(product.getStatus())) {
            criteria.andStatusEqualTo(product.getStatus());
        }
        return productMapper.selectByExample(productExample);
    }

    public int doEdit(Product product) {
        return productMapper.updateByPrimaryKeySelective(product);
    }

    public int doAdd(Product product) {
        return productMapper.insertSelective(product);
    }

    public Product queryDetail(Long id) {
        return productMapper.selectByPrimaryKey(id);
    }

    public int doDel(Product product) {
        return productMapper.deleteByPrimaryKey(product.getId());
    }
}
