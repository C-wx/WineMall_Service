package winemall.controller.fore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import winemall.bean.Addr;
import winemall.dto.Result;
import winemall.service.AddrService;

import java.util.List;

/**
 * @Explain: 地址控制器
 */
@Controller
public class AddrController {

    @Autowired
    private AddrService addrService;

    /**
     * @param openId 用户标识ID
     * @Explain 获取用户的地址列表
     */
    @ResponseBody
    @RequestMapping("/getAddrList")
    public Object getAddrList(String openId) {
        List<Addr> addrList = addrService.queryList(openId);
        return Result.success(addrList);
    }

    /**
     * @param addr 地址传输实体
     * @Explain 更新/删除地址
     */
    @ResponseBody
    @RequestMapping("/doOpeAddr")
    private Object doOpeAddr(Addr addr) {
        int res;
        if (addr.getId() != null) {         //ID不为空说明是更新/删除操作
            res = addrService.doEdit(addr);
        } else {                                //删除操作
            res = addrService.doAdd(addr);
        }
        return res > 0 ? Result.success() : Result.error();
    }

    /**
     * @param id 地址ID
     * @Explain 获取地址详情
     * @Return
     */
    @ResponseBody
    @RequestMapping("/getAddrDetail")
    public Object getAddrDetail(Long id) {
        Addr addr = addrService.queryDetail(id);
        return Result.success(addr);
    }
}
