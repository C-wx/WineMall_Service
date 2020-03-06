package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Image;
import winemall.bean.ImageExample;
import winemall.mapper.ImageMapper;

import java.util.List;

/**
 * @Explain: 图片处理器
 */
@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    public int doAdd(Image image) {
        return imageMapper.insertSelective(image);
    }

    public List<Image> queryList(Long id) {
        ImageExample imageExample = new ImageExample();
        imageExample.createCriteria().andProductIdEqualTo(id).andStatusEqualTo("E");
        imageExample.setOrderByClause("id desc");
        return imageMapper.selectByExample(imageExample);
    }

    public int doEdit(Image image) {
        return imageMapper.updateByPrimaryKeySelective(image);
    }

    public Image queryDetail(Long id) {
        return imageMapper.selectByPrimaryKey(id);
    }
}
