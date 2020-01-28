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
import winemall.dto.Result;
import winemall.service.ImageService;
import winemall.service.ProductService;
import winemall.utils.FileUpload;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Explain: 产品控制器
 */
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

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

    @ResponseBody
    @RequestMapping("/doOpeProduct")
    public Object doOpeProduct(Product product, @RequestParam(value = "files", required = false) MultipartFile[] files, HttpSession session) {
        int res;
        if ("COMPLETE".equals(product.getStatus())) {
            res = productService.doDel(product);
        } else if (product.getId() != null) {
            if (!ArrayUtils.isEmpty(files)) {
                List<String> list_image = FileUpload.upload_image(files, session);
                if (!CollectionUtils.isEmpty(list_image)) {
                    addImage(product, list_image);
                }
            }
            if ("on".equals(product.getIsActive())) {
                product.setIsActive("1");
            } else {
                product.setIsActive("0");
            }
            res = productService.doEdit(product);
        } else {
            Merchant merchant = (Merchant) session.getAttribute("LOGIN_USER");
            product.setUserId(merchant.getId());
            if ("on".equals(product.getIsActive())) {
                product.setIsActive("1");
            } else {
                product.setIsActive("0");
            }
            res = productService.doAdd(product);
            List<String> list_image = FileUpload.upload_image(files, session);
            if (!CollectionUtils.isEmpty(list_image)) {
                addImage(product, list_image);
            }
        }
        return res > 0 ? Result.success() : Result.error("操作失败");
    }

    @RequestMapping("/toOpeProduct")
    public String toOpeProduct(Long id, Model model) {
        if (id != null) {
            Product product = productService.queryDetail(id);
            List<Image> images = imageService.queryList(product.getId());
            if (!CollectionUtils.isEmpty(images)) {
                product.setImageList(images);
            }
            model.addAttribute("product", product);
        }
        return "productOpe";
    }

    @ResponseBody
    @RequestMapping("/doDelImage")
    public Object doDelImage(Long id) {
        Image image = new Image();
        image.setId(id);
        image.setStatus("D");
        int res = imageService.doEdit(image);
        return res > 0 ? Result.success() : Result.error();
    }

    private void addImage(Product product, List<String> list_image) {
        list_image.stream().forEach(li -> {
            Image image = new Image();
            image.setProductId(product.getId());
            image.setUrl(li);
            imageService.doAdd(image);
        });
    }
}
