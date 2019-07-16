package com.xiabuxiabu.storemanage.controller.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.ServicePerson;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.entity.store.WidthBand;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.store.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/adjust")
public class AdjustController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private ItemsService itemsService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private MarketService marketService;
    @Autowired
    private StoreStatusService storeStatusService;
    @Autowired
    private WidthBandService widthBandService;
    @Autowired
    private ServicePersonService servicePersonService;
    @Autowired
    private AccessMethodService accessMethodService;
    /**
     * 修改调整页面（市场IT）
     * @return
     */
    @RequestMapping("/updateAdjust")
    public ModelAndView updateAdjust(int id,ModelAndView modelAndView){
       modelAndView.setViewName("/store/updateadjust");
       modelAndView.addObject("storeId",id);
       return modelAndView;
    }

    /**
     * 市场IT调整门店的基本信息，进行修改
     * 发现修改开业日期时，存入数据库的日总是少一天，这是因为时区设置的问题UTC。需要更改时区的设置。
     * 应该改为serverTimezone=Asia/Shanghai即可
     * @param store
     * @param
     * @return
     */
    @RequestMapping("/toupdateAdjust")
    @ResponseBody
    public Map<String,Object> toupdate(String store){
        //转化为json对象
        Store jsonStore  = JSON.parseObject(store,Store.class);
        System.out.println("门店基本信息---》"+jsonStore);

        Set<Items> itemsSetJSON = jsonStore.getItemsSet();
        Store storeDB = storeService.findById(jsonStore.getStoreId());
        if(storeDB.getItemsSet().size()!=0&&jsonStore.getItemsSet().size()!=0){
            for (Items itemsDB:storeDB.getItemsSet()) {
                for (Items itemsJSON:itemsSetJSON) {
                    if(itemsDB.getId()==itemsJSON.getId()){
                        Items itemsDemo = itemsService.findById(itemsJSON.getId());
                        //表示该设备需要更新
                        if(itemsDemo.getSign().equals("2")){
                            itemsDemo.setClassName(itemsJSON.getClassName());
                            itemsDemo.setEquipName(itemsJSON.getEquipName());
                            itemsDemo.setItem(itemsJSON.getItem());
                            itemsDemo.setNum(itemsJSON.getNum());
                            itemsService.save(itemsDemo);
                        }
                    }
                }
            }
            storeDB.setItemsSet(jsonStore.getItemsSet());
        }
        //添加宽带
        if(storeDB.getWidthBandSet().size()!=0&&jsonStore.getWidthBandSet().size()!=0){
            for (WidthBand widthBandJSON:jsonStore.getWidthBandSet()) {
                for(WidthBand widthBandDB:storeDB.getWidthBandSet()){
                    if(widthBandJSON.getWid()==widthBandDB.getWid()){
                        WidthBand widthBandDemo = widthBandService.findById(widthBandDB.getWid());
                        if(widthBandDemo.getSign().equals("2")){
                            widthBandDemo.setServicePerson(widthBandJSON.getServicePerson());
                            widthBandDemo.setAccessMethod(widthBandJSON.getAccessMethod());
                            widthBandDemo.setPayMethod(widthBandJSON.getPayMethod());
                            widthBandDemo.setPayMoney(widthBandJSON.getPayMoney());
                            widthBandDemo.setIdentity(widthBandJSON.getIdentity());
                            widthBandDemo.setPassword(widthBandJSON.getPassword());
                            widthBandDemo.setTapewidth(widthBandJSON.getTapewidth());
                            widthBandDemo.setEndDate(widthBandJSON.getEndDate());
                            widthBandService.save(widthBandDemo);
                        }
                    }
                }
            }
            storeDB.setWidthBandSet(jsonStore.getWidthBandSet());
        }
        //市场IT调整完后，设置门店的状态为2.待审批状态
        storeDB.setStoreStatus(storeStatusService.findById(2));
        storeService.save(storeDB);
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        return map;
    }
}
