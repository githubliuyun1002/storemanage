package com.xiabuxiabu.storemanage.controller.publicutils;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/market")
public class MarketController {
    @Autowired
    private MarketService marketService;
    @RequestMapping("/marketList")
    @ResponseBody
    public List<MarketEntity> marketEntityList(){
        return marketService.findAll();
    }
}
