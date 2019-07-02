package com.xiabuxiabu.storemanage.controller.store;

import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 门店设备修改日志查询history
 */
@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private StoreService storeService;

    /**
     * 设备修改历史表的展示页面
     * @return
     */
    @RequestMapping("/home")
    public String home(){
        return "/store/itemhistory";
    }
    @RequestMapping("/itemHistoryList")
    @ResponseBody
    public Page<Store> itemHistory(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return storeService.itemHistory(page,pageSize,searchName);
    }




}
