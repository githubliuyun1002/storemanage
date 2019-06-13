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
import net.bytebuddy.asm.Advice;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @Autowired    //宽带运营商
    private ServicePersonService servicePersonService;
    @Autowired     //接入方式
    private AccessMethodService accessMethodService;
    @Autowired     //付款方式
    private PayMethodService payMethodService;
    @Autowired     //保存宽带信息
    private WidthBandService widthBandService;
    @Autowired     //门店所选的设备items
    private ItemsService itemsService;
    @Autowired
    private StoreStatusService storeStatusService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private ItemService itemService;   //item设备型号

    /**
     * 初始化转换日期类，将输入框中String类型的值，转化为Date类型的值。
     * 并设置相应的日期类型
     * @param binder
     * @param request CustomDateEditor为自定义日期编辑器
     */
    @InitBinder
    public void initBinder(WebDataBinder binder, WebRequest request) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    /**
     * 门店展示首页信息
     * @return
     */
    @RequestMapping("/home")
    public String store(){
        return "/store/home";
    }
    @RequestMapping("/findAllList")
    @ResponseBody
    public Page<Store> findAllList(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return storeService.findAll(page,pageSize,searchName);
    }

    /**
     * 门店具体信息展示页面
     * @param id
     * @param modelAndView
     * @return
     */
    @RequestMapping("/content")
    public ModelAndView content(int id,ModelAndView modelAndView){
        modelAndView.setViewName("/store/content");
        modelAndView.addObject("storeId",id);
        return modelAndView;
    }

    /**
     * 门店查询
     * @param storeId
     * @return
     */
    @RequestMapping("/findById")
    @ResponseBody
    public Store findById(int storeId){
        return storeService.findById(storeId);
    }

    /**
     * 为门店添加宽带设备
     * @param storeId
     * @param widthBand
     * @param modelAndView
     * @return
     */
    @RequestMapping("/width")
    public ModelAndView widthSave(int storeId, WidthBand widthBand, ModelAndView modelAndView){
        Store store = storeService.findById(storeId);
        WidthBand saveWidthBand =  widthBandService.save(widthBand);
        store.setWidthBand(saveWidthBand);
        storeService.save(store);
        modelAndView.setViewName("/store/content");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;
        //hibernate中的持久化对象，瞬时对象，脱管对象的区别。
    }
    /**
     * 向门店中添加设备 items
     * @param modelAndView
     * @param storeId
     * @param items
     * @return
     */
    @RequestMapping("/additem")
    public ModelAndView addItem(ModelAndView modelAndView, int storeId,int storeStatus,Items items){
        Store store  =  storeService.findById(storeId);
        StoreStatus storeStatusEntity =  storeStatusService.findById(storeStatus);
        store.setStoreStatus(storeStatusEntity);   //设置门店状态为待审核
        if(store.getItemsSet().size()!=0){
            Set<Items> oldItmsSet = store.getItemsSet();
            Items saveItems = itemsService.save(items);
            oldItmsSet.add(saveItems);
            store.setItemsSet(oldItmsSet);
            storeService.save(store);
        }else{
            Items saveItems =  itemsService.save(items);
            Set<Items> newItemsSet = new HashSet<>();
            newItemsSet.add(saveItems);
            store.setItemsSet(newItemsSet);
            storeService.save(store);
        }
        modelAndView.setViewName("/store/content");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;
    }
    /**
     * 手动添加门店信息
     */
    @RequestMapping("/saveStore")
    public String saveStore(Store store){
        //需要给一个默认的餐厅的状态(待选择)
        StoreStatus storeStatus = storeStatusService.findById(1);
        store.setStoreStatus(storeStatus);
        storeService.save(store);
        return "redirect:/store/home";
    }
    /**
     * 宽带运营商List
     * @return
     */
    @RequestMapping("/serviceperson")
    @ResponseBody
    public List<ServicePerson> findAllService(){
        return servicePersonService.findAll();
    }

    /**
     * 宽带接入方式List
     * @return
     */
    @RequestMapping("/accessmethod")
    @ResponseBody
    public List<AccessMethod> findAllAccess(){
        return accessMethodService.findAll();
    }

    /**
     * 宽带付款方式List
     * @return
     */
    @RequestMapping("/paymethod")
    @ResponseBody
    public List<PayMethod> fingAllPayMethod(){
        return payMethodService.findAll();
    }
    @RequestMapping("/marketAll")
    @ResponseBody
    public List<MarketEntity> marketAll(){
        return marketService.findAll();
    }

    /**
     * 修改页面回显
     * @param id
     * @param modelAndView
     * @return
     */
    @RequestMapping("/update")
    public ModelAndView update(int id,ModelAndView modelAndView){
        modelAndView.setViewName("/store/storeupdate");
        modelAndView.addObject("storeId",id);
        return modelAndView;
    }
    @RequestMapping("/toupdate")
    @ResponseBody
    public Map<String,Object> toupdate(String store,String itemsSet){
        //转换为json对象
        Store jsonStore  = JSON.parseObject(store,Store.class);
        //对set中的数据更新
        Set<Items> items = new HashSet<>(JSONObject.parseArray(itemsSet,Items.class));
        Store saveStore = storeService.findById(jsonStore.getStoreId());
        if(saveStore.getItemsSet().size()!=0){
            for (Items itemsJSON: items) {
                for (Items itemsDB:saveStore.getItemsSet()) {
                    if(itemsDB.getId()==itemsJSON.getId()){
                        Items itemsDemo = itemsService.findById(itemsDB.getId());
                        itemsDemo.setId(itemsJSON.getId());
                        itemsDemo.setClassName(itemsJSON.getClassName());
                        itemsDemo.setEquipName(itemsJSON.getEquipName());
                        if(itemsDemo.getItem()!=null){
                            if(itemsDemo.getItem().getItemId()==itemsJSON.getItem().getItemId()){
                                Item item = itemService.findById(itemsDemo.getItem().getItemId());
                                item.setItemId(itemsJSON.getItem().getItemId());
                                item.setName(itemsJSON.getItem().getName());
                                item.setSign(itemsJSON.getItem().getSign());
                                itemService.save(item);
                                itemsDemo.setItem(item);
                            }
                        }
                        itemsDemo.setNum(itemsJSON.getNum());
                        itemsService.save(itemsDemo);
                        saveStore.getItemsSet().add(itemsDemo);
                    }
                }
            }
        }
        List<String> signList = new ArrayList<>();
        for (Items setItems:items) {
            signList.add(setItems.getItem().getSign());
        }
        //包含2说明需要市场IT调整
        if(signList.contains("2")){
            saveStore.setStoreStatus(storeStatusService.findById(3));  //状态为待调整
        }else{
            saveStore.setStoreStatus(storeStatusService.findById(4));  //状态为已确认
        }
        storeService.save(saveStore);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code","1");
        return map;
    }
    //待调整页面显示
    @RequestMapping("/adjustment")
    public String adjustment(){
        return "/store/adjustore";   //adjustore
    }
    @RequestMapping("/adjustList")
    @ResponseBody
    public Page<Store> adjustList(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return storeService.adjustList(page,pageSize,searchName);
    }








}
