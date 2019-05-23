/*
package com.xiabuxiabu.storemanage.controller.store;


import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.*;

import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.store.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import net.sf.json.JSONObject;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private StoreStatusService storeStatusService;
    @Autowired
    private ServicePersonService servicePersonService;
    @Autowired
    private AccessMethodService accessMethodService;
    @Autowired
    private PayMethodService payMethodService;
    @Autowired
    private WidthBandService widthBandService;
    @Autowired
    private StoreChangeService storeChangeService;

    */
/* * 初始化转换日期类，将输入框中String类型的值，转化为Date类型的值。
     * 并设置相应的日期类型
     *
     * @param binder
     * @param request CustomDateEditor为自定义日期编辑器*//*

    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    @RequestMapping("/home")
    public String home(){
        return "/store/home";
    }
     */
/**
     * 查询List数据
      * *//*



    @RequestMapping("/findAll")
    @ResponseBody
    public Page<Store> findAll(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return  storeService.findAll(page,pageSize,searchName);
    }
*/
/**
     * 为门店添加具体设备的界面*//*



    @RequestMapping("/equipsets")
    public ModelAndView equipsets(@RequestParam("id") int id,ModelAndView modelAndView){
        Store store = storeService.findById(id);
        //将java对象转化为json对象
        JSONObject jsonObject = JSONObject.fromObject(store.toString());
        modelAndView.setViewName("/store/store_equip");
        modelAndView.addObject("StoreMsg",jsonObject);
        modelAndView.addObject("StoreId",id);
        return modelAndView;
    }
    @RequestMapping("/findById")
    @ResponseBody
    public Store findById(@RequestParam("id") int id){

        return storeService.findById(id);
    }
*/
/*@RequestMapping("/addEquip")
    public ModelAndView addEquip(ModelAndView modelAndView, StoreType storeType,int store_status){
        System.out.println("storeType--->"+storeType);
        if(storeType!=null){
            int storeId = storeType.getStoreId();
            int typeId = storeType.getTypeId();
            Store store = storeService.findById(storeId);
            StoreStatus storeStatus = storeStatusService.findById(store_status);
            store.setStoreStatus(storeStatus);
            System.out.println("store--->"+store);
            JSONObject storeObj = JSONObject.fromObject(store.toString());
            modelAndView.addObject("StoreMsg",storeObj);
            modelAndView.addObject("StoreId",store.getId());
            StoreType storeType1 = storeTypeService.findStore(storeId,typeId);
          //  System.out.println(storeTypeService.findStore(storeId,typeId)!=null);
            if(storeType1!=null){
                 storeType.setId(storeType1.getId());
            }
            storeTypeService.save(storeType);
        }
         modelAndView.setViewName("/store/store_equip");
         return  modelAndView;
    }*//*

    */
/*@RequestMapping("/storeTypeList")
    @ResponseBody
    public List<StoreType> storeTypeList(@RequestParam("storeId") int storeId){
        return storeTypeService.findTypeByStoreId(storeId);
    }
    @RequestMapping("/findByTypeId")
    @ResponseBody
    public TypeEntity findByTypeId(@RequestParam("typeId") int typeId){
        return  typeService.findById(typeId);
    }*//*

    @RequestMapping("/saveStore")
    public String saveStore(Store store){
        System.out.println("store--->"+store);
        storeService.save(store);
        return "redirect:/store/home";
    }

    @RequestMapping("/marketList")
    @ResponseBody
    public List<MarketEntity> marketList(){

        return marketService.findAll();
    }
    @RequestMapping("/storeStatusList")
    @ResponseBody
    public List<StoreStatus> storeStatusList(){

        return  storeStatusService.findAll();
    }
    @RequestMapping("/servicePersonList")
    @ResponseBody
    public List<ServicePerson> servicePersonList(){

        return servicePersonService.findAll();
    }
    @RequestMapping("/accessMethodList")
    @ResponseBody
    public List<AccessMethod> accessMethodList(){

        return accessMethodService.findAll();
    }
    @RequestMapping("/payMethodList")
    @ResponseBody
    public List<PayMethod> payMethodList(){

        return  payMethodService.findAll();
    }

    @RequestMapping("/storeWidth")
    public ModelAndView storeWidth(ModelAndView modelAndView,WidthBand widthBand,int storeId)  {
        Store store = storeService.findById(storeId);
        widthBandService.save(widthBand);
        store.setWidthBand(widthBand);
        store = storeService.save(store);
        JSONObject storeObj = JSONObject.fromObject(store.toString());
        System.out.println("store--->"+storeObj);
        modelAndView.addObject("StoreMsg",storeObj);
        modelAndView.addObject("StoreId",store.getId());
        modelAndView.setViewName("/store/store_equip");
        return modelAndView;
    }
*/
/**
     * 回显餐厅信息*//*



    @RequestMapping("/update")
    @ResponseBody
    public  ModelAndView update(int id,ModelAndView modelAndView){
        Store store = storeService.findById(id);
        modelAndView.addObject("storeObj",store);
        modelAndView.setViewName("/store/store_update");
        return modelAndView;
    }
*/
/*
*
     * 执行修改餐厅信息
*//*



    @RequestMapping("/toupdate")
    public String toupdate(Store store, WidthBand widthBand){
        if(widthBand.getWid()!=0){
            if(widthBandService.findById(widthBand.getWid())!=null){
                widthBandService.save(widthBand);
            }
            if(storeService.findById(store.getId())!=null){
                store.setWidthBand(widthBand);
                storeService.save(store);
            }
        }else{
            if(storeService.findById(store.getId())!=null){
                storeService.save(store);
            }
        }
        return "redirect:/store/home";
    }

*/
/**
     * 显示设别变更界面
     * @param typeId
     * @param storeId
     * @param id
     * @param modelAndView
     * @return*//*


   */
/* @RequestMapping("/equipChange")
    @ResponseBody
    public ModelAndView equipChange(int typeId,int storeId,int id,ModelAndView modelAndView){
        StoreType storeType = storeTypeService.findById(id);
        Store store = storeService.findById(storeId);
        TypeEntity type = typeService.findById(typeId);
        modelAndView.addObject("type",type);
        modelAndView.addObject("store",store);
        modelAndView.addObject("storeType",storeType);
        modelAndView.setViewName("/store/store_TypeChange");
        return  modelAndView;
    }*//*



*/
/**
     * 设置变更
     * @param modelAndView
     * @param id
     * @param num
     * @param
     * @return*//*


    */
/*@RequestMapping("/storeTypeChange")
    public ModelAndView storeTypeChange(ModelAndView modelAndView,int id,int num,StoreChange storeChange){
        StoreType storeType = storeTypeService.findById(id);
        Store store = storeService.findById(storeType.getStoreId());
        JSONObject jsonObject = JSONObject.fromObject(store.toString());
        modelAndView.addObject("StoreMsg",jsonObject);
        modelAndView.addObject("StoreId",store.getId());
        storeType.setNum(num);
        storeTypeService.save(storeType);
        modelAndView.setViewName("/store/store_equip");
        System.out.println("storeChange------>"+storeChange);
        storeChange.setStoreId(storeType.getStoreId());
        storeChangeService.save(storeChange);
        return modelAndView;
    }*//*


*/
/**
     * 变更历史查询页面*//*



   */
/* @RequestMapping("/historySearch")
    public ModelAndView historySearch(int storeId,ModelAndView modelAndView){

        modelAndView.addObject("storeId",storeId);
        modelAndView.setViewName("/store/store_history");
        return modelAndView;
    }
    @RequestMapping("/storeChangeList")
    @ResponseBody
    public List<StoreChange> storeChangeList(int storeId){
        return storeChangeService.findByStoreId(storeId);
    }*//*

*/
/**
     * 信息推送页面


    @RequestMapping("/store_Message")
    public String store_Message(){
        return "/store/store_Message";
    }*//*

*/
/*
*
     * 信息推送页面数据展示
*//*



*/
/*@RequestMapping("/storeMesData")
    @ResponseBody
    public Page<Store> storeMesData(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize){

    }*//*



}
*/
