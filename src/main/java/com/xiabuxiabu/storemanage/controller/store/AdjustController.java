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
     * @param itemsSet
     * @return
     */
    @RequestMapping("/toupdateAdjust")
    @ResponseBody
    public Map<String,Object> toupdate(String store,String itemsSet){
        //转化为json对象
        Store storeJson =  JSON.parseObject(store,Store.class);
        Set<Items> items = new HashSet<>(JSONObject.parseArray(itemsSet,Items.class));
        Store saveStore = storeService.findById(storeJson.getStoreId());
        //修改门店基本信息
        saveStore.setStoreCode(storeJson.getStoreCode());
        saveStore.setStoreName(storeJson.getStoreName());
        saveStore.setAddress(storeJson.getAddress());
        saveStore.setMarger(storeJson.getMarger());
        saveStore.setOpenDate(storeJson.getOpenDate());
        //闭店日期字段
        saveStore.setCloseDate(storeJson.getCloseDate());
        //市场对象的修改（市场id---》市场对象）
        if(marketService.findMarketByMarketName(storeJson.getMarketCode().getName())!=null){
            MarketEntity marketEntity = marketService.findMarketByMarketName(storeJson.getMarketCode().getName());
            saveStore.setMarketCode(marketEntity);
        }
        //填充页面宽带的基本信息(数据库中宽带信息)
        WidthBand widthBandDB =  widthBandService.findById(storeJson.getWidthBand().getWid());
        WidthBand widthBandJson = storeJson.getWidthBand();
        widthBandDB.setPayMoney(widthBandJson.getPayMoney());
        widthBandDB.setIdentity(widthBandJson.getIdentity());
        widthBandDB.setPassword(widthBandJson.getPassword());
        widthBandDB.setTapewidth(widthBandJson.getTapewidth());
        //设置宽带的服务商
        if(servicePersonService.findByServiceName(widthBandJson.getServicePerson().getServiceName())!=null) {
            ServicePerson servicePerson = servicePersonService.findByServiceName(widthBandJson.getServicePerson().getServiceName());
            widthBandDB.setServicePerson(servicePerson);
        }
        //修改门店设备基本信息
        if(saveStore.getItemsSet().size()!=0){
            for (Items itemsJSON: items) {
                for (Items itemsDB:saveStore.getItemsSet()) {
                    if(itemsDB.getId()==itemsJSON.getId()){
                        Items itemsDemo = itemsService.findById(itemsDB.getId());
                        //"2"表示需要进行更新
                        if(itemsDemo.getSign().equals("2")){
                            itemsDemo.setId(itemsJSON.getId());
                            itemsDemo.setClassName(itemsJSON.getClassName());
                            itemsDemo.setEquipName(itemsJSON.getEquipName());
                            if(itemsDemo.getItem()!=null){
                                if(itemsDemo.getItem().getItemId()==itemsJSON.getItem().getItemId()){
                                    Item item = itemService.findById(itemsDemo.getItem().getItemId());
                                    item.setItemId(itemsJSON.getItem().getItemId());
                                    item.setName(itemsJSON.getItem().getName());
                                    itemsDemo.setItem(item);
                                }
                            }
                            //记录原来的设备数量
                            itemsDemo.setOrigin(itemsDemo.getNum());
                            itemsDemo.setNum(itemsJSON.getNum());
                            itemsDemo.setSign(itemsDemo.getSign());
                            itemsDemo.setPersonName((String) httpServletRequest.getSession().getAttribute("userName"));
                            itemsDemo.setUpdateTime(new Date());
                            itemsService.save(itemsDemo);
                            saveStore.getItemsSet().add(itemsDemo);
                        }
                    }
                }
            }
        }
        //市场IT调整完后，设置门店的状态为2.待审批状态
        saveStore.setStoreStatus(storeStatusService.findById(2));
        storeService.save(saveStore);
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        return map;
    }
}
