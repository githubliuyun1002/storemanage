package com.xiabuxiabu.storemanage.controller.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.*;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.store.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
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
    private StoreRemarksService storeRemarksService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

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
     *
     * ---------》市场IT调整
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
//                        //####
//                        itemsDemo.setClassName(itemsJSON.getClassName());
//                        itemsDemo.setEquipName(itemsJSON.getEquipName());
//                        itemsDemo.setItem(itemsJSON.getItem());
//                        if(itemsJSON.getNum()!=itemsDemo.getNum()){
//                            itemsDemo.setSign("2");
//                            itemsDemo.setNum(itemsJSON.getNum());
//
//                        }
//                        itemsService.save(itemsDemo);
                        //######

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
        }
        //市场IT调整完后，设置门店的状态为2.待审批状态
        storeDB.setStoreStatus(storeStatusService.findById(2));
        storeService.save(storeDB);
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        return map;
    }

    /**
     * 当门店为待调整状态时，为门店添加相应的设备信息
     */
    @RequestMapping("/addwidthInfor")
    public ModelAndView addwidthInfor(int storeId, WidthBand widthBand, ModelAndView modelAndView){
        Store store = storeService.findById(storeId);
        Set<WidthBand> widthBandSet = store.getWidthBandSet();
        //宽带设置为待审批装态
        widthBand.setSign("2");
        widthBandService.save(widthBand);
        widthBandSet.add(widthBand);
        store.setWidthBandSet(widthBandSet);
        storeService.save(store);
        modelAndView.setViewName("/store/updateadjust");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;
    }
    /**
     * 当门店为调整状态，再次添加门店的设备信息
     */
    @RequestMapping("/additemInfor")
    public ModelAndView additemInfor(ModelAndView modelAndView, int storeId,Items items){
        Store store  =  storeService.findById(storeId);

        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        StoreRemarks storeRemarks = new StoreRemarks();
        storeRemarks.setStoreName(store.getStoreName());
        storeRemarks.setStoreCode(store.getStoreCode());
        storeRemarks.setMarketName(store.getMarketName());
        storeRemarks.setItemName(items.getItem().getName());
        storeRemarks.setItemId(items.getItem().getItemId());
        //操作人
        storeRemarks.setOperatePerson(userName);
        //设备原来的数量为0
        storeRemarks.setOrignnum(0);
        //设备现在的数量
        storeRemarks.setNownum(items.getNum());
        //设备变化量
        storeRemarks.setChangenum(items.getNum()-0);
        storeRemarks.setStoreAnditem(store.getStoreName()+""+items.getItem().getName());
        //设置操作记录的时间
        storeRemarks.setUpdateTime(new Date());
        storeRemarksService.save(storeRemarks);
        //状态为待审批状态
        items.setSign("2");
        itemsService.save(items);

        Set<Items> itemsSet = store.getItemsSet();
        itemsSet.add(items);
        store.setItemsSet(itemsSet);

        storeService.save(store);
        modelAndView.setViewName("/store/updateadjust");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;
    }


}
