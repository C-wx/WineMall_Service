package winemall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import winemall.bean.Image;
import winemall.bean.Merchant;
import winemall.bean.Product;
import winemall.bean.Property;
import winemall.dto.PropertyDto;
import winemall.dto.Result;
import winemall.service.ImageService;
import winemall.service.ProductService;
import winemall.service.PropertyService;
import winemall.utils.QiniuCloudUtil;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * @Explain: 产品控制器
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PropertyService propertyService;

    @RequestMapping("/toProductManage")
    public String toProductManage() {
        return "index";
    }

    @RequestMapping("/toProductRecycle")
    public String toProductRecycle() {
        return "productRecycle";
    }

    /**
     * @param pn    页码
     * @param size  每页的数据量
     * @param sort  过滤条件
     * @param order 分类条件
     * @Explain 获取历史商品
     */
    @ResponseBody
    @GetMapping("/productPage")
    public Object productPage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                              @RequestParam(value = "size", defaultValue = "10") Integer size,
                              @RequestParam(value = "sort", defaultValue = "id") String sort,
                              @RequestParam(value = "order", defaultValue = "desc") String order,
                              String name) {
        PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  size：页大小
        Product product = new Product();
        product.setStatus("E");
        if (StringUtils.isNotBlank(name)) {
            product.setName(name);
        }
        List<Product> productList = productService.queryList(product);
        PageInfo pageInfo = new PageInfo(productList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * @param pn    页码
     * @param size  每页的数据量
     * @param sort  过滤条件
     * @param order 分类条件
     * @Explain 获取历史删除商品
     */
    @ResponseBody
    @GetMapping("/productRecyclePage")
    public Object productRecyclePage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size,
                                     @RequestParam(value = "sort", defaultValue = "id") String sort,
                                     @RequestParam(value = "order", defaultValue = "desc") String order,
                                     String name) {
        PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  size：页大小
        Product product = new Product();
        product.setStatus("D");
        if (StringUtils.isNotBlank(name)) {
            product.setName(name);
        }
        List<Product> productList = productService.queryList(product);
        PageInfo pageInfo = new PageInfo(productList, 10);
        return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * @param product     商品传输实体
     * @param files       图片流
     * @param propertyDto 属性传输实体
     * @Explain 删除/修改/添加  商品操作
     */
    @ResponseBody
    @RequestMapping("/doOpeProduct")
    public Object doOpeProduct(PropertyDto propertyDto, Product product,
                               @RequestParam(value = "files", required = false) MultipartFile[] files,
                               HttpSession session) {
        int res;
        if ("COMPLETE".equals(product.getStatus())) {       //删除商品操作
            res = productService.doDel(product);
        } else if (product.getId() != null) {               //编辑商品操作
            if (!ArrayUtils.isEmpty(files)) {
                doUploadImage(product, files);
            }
            if ("on".equals(product.getIsActive())) {       // 是否参加活动   1：参加 0：不参加
                product.setIsActive("1");
            } else {
                product.setIsActive("0");
            }
            res = productService.doEdit(product);
        } else {                                            //添加商品操作
            Merchant merchant = (Merchant) session.getAttribute("LOGIN_USER");
            product.setUserId(merchant.getId());
            if ("on".equals(product.getIsActive())) {
                product.setIsActive("1");
            } else {
                product.setIsActive("0");
            }
            res = productService.doAdd(product);
            doUploadImage(product, files);
            // 为商品属性赋值
            Property property = new Property();
            if (StringUtils.isNotBlank(propertyDto.getVariety())) {
                property.setName("品种");
                property.setValue(propertyDto.getVariety());
                property.setProductId(product.getId());
                propertyService.doAdd(property);
            }
            if (StringUtils.isNotBlank(propertyDto.getType())) {
                property.setName("类型");
                property.setValue(propertyDto.getType());
                property.setProductId(product.getId());
                propertyService.doAdd(property);
            }
            if (StringUtils.isNotBlank(propertyDto.getYears())) {
                property.setName("年份");
                property.setValue(propertyDto.getYears());
                property.setProductId(product.getId());
                propertyService.doAdd(property);
            }
            if (StringUtils.isNotBlank(propertyDto.getCapacity())) {
                property.setName("产品规格");
                property.setValue(propertyDto.getCapacity());
                property.setProductId(product.getId());
                propertyService.doAdd(property);
            }
            if (StringUtils.isNotBlank(propertyDto.getDegree())) {
                property.setName("酒精度数");
                property.setValue(propertyDto.getDegree());
                property.setProductId(product.getId());
                propertyService.doAdd(property);
            }
        }
        return res > 0 ? Result.success() : Result.error("操作失败");
    }

    /**
     * @Explain 上传图片
     */
    private void doUploadImage(Product product, @RequestParam(value = "files", required = false) MultipartFile[] files) {
        for (MultipartFile myFile : files) {
            if (StringUtils.isNotBlank(myFile.getOriginalFilename())) {
                try {
                    byte[] bytes = myFile.getBytes();
                    String imageName = UUID.randomUUID().toString();
                    String url = QiniuCloudUtil.put64image(bytes, imageName);
                    Image image = new Image();
                    image.setProductId(product.getId());
                    image.setUrl(url);
                    imageService.doAdd(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param id 商品ID
     * @Explain 跳转商品操作页
     */
    @RequestMapping("/toOpeProduct")
    public String toOpeProduct(Long id, Model model) {
        if (id != null) {
            Product product = productService.queryDetail(id);
            List<Image> images = imageService.queryList(product.getId());
            if (!CollectionUtils.isEmpty(images)) {
                product.setImageList(images);
            }
            List<Property> propertyList = propertyService.queryList(product.getId());
            model.addAttribute("ope", "Edit");
            model.addAttribute("product", product);
            model.addAttribute("propertyList", propertyList);
        } else {
            model.addAttribute("ope", "Add");
        }
        return "productOpe";
    }

    /**
     * @param id 图片ID
     * @Explain 删除图片
     */
    @ResponseBody
    @RequestMapping("/doDelImage")
    public Object doDelImage(Long id) {
        Image image = imageService.queryDetail(id);
        String target = image.getUrl().split("/")[3];
        QiniuCloudUtil.delete(target);
        image.setStatus("D");
        int res = imageService.doEdit(image);
        return res > 0 ? Result.success() : Result.error();
    }
}
