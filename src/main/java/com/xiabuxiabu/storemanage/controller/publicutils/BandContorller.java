package com.xiabuxiabu.storemanage.controller.publicutils;

import com.xiabuxiabu.storemanage.entity.publicutil.Band;
import com.xiabuxiabu.storemanage.service.publicutil.BandService;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/band")
public class BandContorller {
    @Autowired
    private BandService bandService;
    @Autowired
    private StoreBOHService storeBOHService;
    @RequestMapping("/bandList")
    @ResponseBody
    public List<Band> findAllBand(){
       return bandService.findAllBand();
    }

}
