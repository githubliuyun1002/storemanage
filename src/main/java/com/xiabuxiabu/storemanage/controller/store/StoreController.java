package com.xiabuxiabu.storemanage.controller.store;

import com.xiabuxiabu.storemanage.entity.equip.EquipEntity;
import com.xiabuxiabu.storemanage.entity.equip.TypeEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.*;
import com.xiabuxiabu.storemanage.service.equip.EquipService;
import com.xiabuxiabu.storemanage.service.equip.TypeService;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.store.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private StoreTypeService storeTypeService;
    @Autowired
    private TypeService typeService;
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
    /**
     * 初始化转换日期类，将输入框中String类型的值，转化为Date类型的值。
     * 并设置相应的日期类型
     *
     * @param binder
     * @param request CustomDateEditor为自定义日期编辑器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    @RequestMapping("/home")
    public String home(){
        return "/store/home";
    }
    /**
     * 查询List数据
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public Page<Store> findAll(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return  storeService.findAll(page,pageSize,searchName);
    }
    /**
     * 为门店添加具体设备的界面
     */
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
    @RequestMapping("/addEquip")
    public ModelAndView addEquip(ModelAndView modelAndView, StoreType storeType,int store_status){
        System.out.println("store_status---->"+store_status);
        if(storeType!=null){
            int storeId = storeType.getStoreId();
            Store store = storeService.findById(storeId);
            JSONObject storeObj = JSONObject.fromObject(store.toString());
            modelAndView.addObject("StoreMsg",storeObj);
            modelAndView.addObject("StoreId",store.getId());
            storeTypeService.save(storeType);
        }
         modelAndView.setViewName("/store/store_equip");
         return  modelAndView;
    }
    @RequestMapping("/storeTypeList")
    @ResponseBody
    public List<StoreType> storeTypeList(@RequestParam("storeId") int storeId){
        return storeTypeService.findTypeByStoreId(storeId);
    }
    @RequestMapping("/findByTypeId")
    @ResponseBody
    public TypeEntity findByTypeId(@RequestParam("typeId") int typeId){
        return  typeService.findById(typeId);
    }
    @RequestMapping("/saveStore")
    public String saveStore(Store store, WidthBand widthBand,PayMethod payMethod,AccessMethod accessMethod){
        System.out.println("store---->"+store);
        System.out.println("widthBand---->"+widthBand);
        System.out.println("pay--->"+payMethod);
        System.out.println("access--->"+accessMethod);
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

}
