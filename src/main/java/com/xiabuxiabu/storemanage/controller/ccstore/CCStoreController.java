package com.xiabuxiabu.storemanage.controller.ccstore;

import com.alibaba.fastjson.JSON;
import com.xiabuxiabu.storemanage.entity.ccequip.CCEquipInfo;
import com.xiabuxiabu.storemanage.entity.ccequip.ccEquip;
import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;
import com.xiabuxiabu.storemanage.service.cceuipservice.CCEquipInfoService;
import com.xiabuxiabu.storemanage.service.cceuipservice.CCEquipService;
import com.xiabuxiabu.storemanage.service.cceuipservice.CCItemsService;
import com.xiabuxiabu.storemanage.service.ccstore.CCStoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ccstore")
public class CCStoreController {
    @Autowired
    private CCStoreService ccStoreService;
    @Autowired
    private CCItemsService ccItemsService;
    @Autowired
    private CCEquipService ccEquipService;
    @Autowired
    private CCEquipInfoService ccEquipInfoService;

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    /**
     * 具体信息展示页面
     * @return
     */
    @RequestMapping("/home")
    public String home(){

        return "/ccstore/ccstore";
    }

    /**
     * 门店所有得设备信息
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public Page<ccItems> findAll(int page, int pageSize, String searchName){
        return ccItemsService.findAll(page,pageSize,searchName);
    }

    /**
     * 门店具体设备信息展示页面
     *
     */
    @RequestMapping("/content")
    public ModelAndView content(ModelAndView modelAndView,int id){
        modelAndView.addObject("storeId",id);
        modelAndView.setViewName("/ccstore/ccontent");
        return modelAndView;
    }
    @RequestMapping("/findById")
    @ResponseBody
    public CCStore update(int id){
        return  ccStoreService.findById(id);
    }

    @RequestMapping("/additems")
    public ModelAndView additem(ModelAndView modelAndView, int storeId, ccItems ccItems){

        CCStore ccStore = ccStoreService.findById(storeId);
        //门店已经选怎了相应的设备
        if(ccStore.getCcItemsSet().size()!=0){
            Set ccItemsSet = ccStore.getCcItemsSet();
            ccItems.setKeywords(ccItems.getCcEquip().getName());
            ccItemsService.save(ccItems);
            //
            ccItemsSet.add(ccItems);
            ccStoreService.save(ccStore);

        }else{
            ccItems.setKeywords(ccItems.getCcEquip().getName());
            ccItemsService.save(ccItems);
            Set ccItemsSet = new HashSet();
            ccItemsSet.add(ccItems);
            ccStore.setCcItemsSet(ccItemsSet);
            ccStoreService.save(ccStore);
        }
        modelAndView.setViewName("/ccstore/ccontent");
        modelAndView.addObject("storeId",storeId);
        return  modelAndView;
    }
    @RequestMapping("/equipAll")
    @ResponseBody
    public List<ccEquip>  equipAll(){
        return ccEquipService.findAll();
    }
    @RequestMapping("/deleteItemsById")
    @ResponseBody
    public Map<String,Object> deleteItemsById(int id){
        Map<String,Object> map = new HashMap<>();
        ccItemsService.deleteById(id);
        map.put("code","true");
        return map;
    }

    /**
     * 保存门店设备信息
     * @param store
     * @return
     */
    @RequestMapping("/saveStoreInfor")
    @ResponseBody
    public Map<String,Object> saveStoreInfor(String store){
        Map<String,Object> map = new HashMap<>();
        map.put("code","true");
        CCStore ccStoreJSON  = JSON.parseObject(store,CCStore.class);
        CCStore ccStoreDB = ccStoreService.findById(ccStoreJSON.getStoreId());
        if(ccStoreDB.getCcItemsSet().size()!=0){
            for(ccItems ccItemDB:  ccStoreDB.getCcItemsSet()){
                for(ccItems ccItemsJSON : ccStoreJSON.getCcItemsSet()){
                    if(ccItemDB.getItemsId()==ccItemsJSON.getItemsId()){
                        ccItems ccItemsDemo =  ccItemsService.findById(ccItemDB.getItemsId());
                        ccItemsDemo.setDays(ccItemsJSON.getDays());
                        ccItemsDemo.setLastDate(ccItemsJSON.getLastDate());
                        ccItemsDemo.setCcEquip(ccItemsJSON.getCcEquip());
                        ccItemsService.save(ccItemsDemo);
                        //#####保存设备信息
                        CCEquipInfo ccEquipInfo = new CCEquipInfo();
                        ccEquipInfo.setStoreCode(ccStoreDB.getStoreCode());
                        ccEquipInfo.setStoreName(ccStoreDB.getStoreName());
                        ccEquipInfo.setEquipName(ccItemsDemo.getCcEquip().getName());
                        ccEquipInfo.setDays(ccItemsJSON.getDays());
                        ccEquipInfo.setWeeks(ccItemsJSON.getLastDate());
                        ccEquipInfo.setMarketName(ccStoreDB.getMarketName());
                        ccEquipInfoService.save(ccEquipInfo);

                    }

                }
            }
            ccStoreDB.setCcItemsSet(ccStoreJSON.getCcItemsSet());
        }
        ccStoreService.save(ccStoreDB);
       // System.out.println("store----->"+ccStoreJSON);
        return  map;
    }

    /**
     * 整合门店得列表信息
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/findAllList")
    @ResponseBody
    public Page<CCStore> findAllList(int page, int pageSize, String searchName){
        return ccStoreService.findAllList(page,pageSize,searchName);
    }










}
