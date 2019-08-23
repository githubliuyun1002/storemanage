package com.xiabuxiabu.storemanage.controller.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.*;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.CompareProperties;
import com.xiabuxiabu.storemanage.publicutils.DateTool;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.publicutils.WidthCheck;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.store.*;
import com.xiabuxiabu.storemanage.service.user.UserService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.rmi.MarshalledObject;
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
    @Autowired
    private HttpServletRequest httpServletRequest;  //拿到sesssion中得登录人
    @Autowired
    private MailListSerivice mailListSerivice;
    @Autowired
    private StoreRemarksService storeRemarksService;

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
    /**
     * content页面中返回页面按钮的点击事件
     */
    @RequestMapping("/homeBtn")
    @ResponseBody
    public Map<String,Object> homeBtn(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","true");
        return  map;
    }



    /**
     * 页面展示按钮
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
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
        //若已经有宽带信息
        if(store.getWidthBandSet().size()!=0){
            Set<WidthBand> oldwidthBandSet = store.getWidthBandSet();
            //“2”标识需要进行修改
            widthBand.setSign("2");
            WidthBand saveWidthBand = widthBandService.save(widthBand);
            oldwidthBandSet.add(saveWidthBand);
            storeService.save(store);
        }else{
            widthBand.setSign("2");
            //hibernate中的持久化对象，瞬时对象，脱管对象的区别。
            WidthBand saveWidthBand = widthBandService.save(widthBand);
            Set<WidthBand> widthBandSet = new HashSet<>();
            widthBandSet.add(saveWidthBand);
            store.setWidthBandSet(widthBandSet);
            storeService.save(store);
        }
        modelAndView.setViewName("/store/content");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;

    }
    /**
     * 向门店中添加设备 items
     * @param modelAndView
     * @param storeId
     * @param items
     * @return
     * 添加设备后，等在页面确认按钮后，改变门店的状体为待审批状态。
     *
     */
    @RequestMapping("/additem")
    public ModelAndView addItem(ModelAndView modelAndView, int storeId,Items items){
         System.out.println("items------>"+items);
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        Store store  =  storeService.findById(storeId);

        //设置门店的展示样式，再根据门店的基本信息进行填充
        if(store.getItemsSet().size()!=0){
            Set<Items> oldItmsSet = store.getItemsSet();
            //对于新添加的设备，默认状态为待审核状态
            items.setSign("2");
            items.setOpreationPerson(userName);
            items.setUpDateTime(new Date());

            Items saveItems = itemsService.save(items);
            oldItmsSet.add(saveItems);
            store.setItemsSet(oldItmsSet);
            storeService.save(store);

        }else{
            //新添加的设别，默认都需要进行验证
            items.setSign("2");
            items.setOpreationPerson(userName);
            items.setUpDateTime(new Date());
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
        System.out.println("store------>"+store);
        //需要给一个默认的餐厅的状态(待选择)
        StoreStatus storeStatus = storeStatusService.findById(1);
        store.setStoreStatus(storeStatus);
        storeService.save(store);
        MailList mailList = new MailList();
        mailList.setStoreCode(store.getStoreCode());
        mailList.setStoreName(store.getStoreName());
        mailList.setStoreStatus("待选择");
        //未发邮件此时的状态为0-----》发送成功后此时的状态为1
        mailList.setMailStatus(0);
        mailList.setMarketName(store.getMarketName());

        mailListSerivice.save(mailList);

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
     * 门店信息确认展示列表：storeupdate
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

    /**
     * 门店信息确认展示列表
     * 管理员的待审批页面的url--------->管理员审批
     *
     * step1:管理员审批过以后，再添加到相应的设备
     *
     * @param store
     * @return
     */
    @RequestMapping("/toupdate")
    @ResponseBody
    public Map<String,Object> toupdate(String store){
        System.out.println("store------>"+store);

        //转换为json对象
        Store jsonStore  = JSON.parseObject(store,Store.class);
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        Set<Items> itemsSetJSON = jsonStore.getItemsSet();
        Set<WidthBand> widthBandSet = jsonStore.getWidthBandSet();
        Store storeDB = storeService.findById(jsonStore.getStoreId());
        List<StoreRemarks> storeRemarksList = storeRemarksService.findAll();

        if(storeDB.getItemsSet().size()!=0){
            for (Items itemsDB:storeDB.getItemsSet()) {
                for (Items itemsJSON:itemsSetJSON) {
                    if(itemsDB.getId()==itemsJSON.getId()){
                        Items itemsDemo = itemsService.findById(itemsJSON.getId());
                        itemsDemo.setClassName(itemsJSON.getClassName());
                        itemsDemo.setEquipName(itemsJSON.getEquipName());
                        itemsDemo.setItem(itemsJSON.getItem());
                        itemsDemo.setNum(itemsJSON.getNum());
                        itemsDemo.setSign(itemsJSON.getSign());
                        itemsDemo.setCheckPerson(userName);
                        itemsDemo.setChenckTime(new Date());
                        for (int i = 0; i <storeRemarksList.size() ; i++) {
                            StoreRemarks storeRemarks = storeRemarksList.get(i);
                            if(storeRemarks.getItemId()==itemsDemo.getItem().getItemId()&&storeRemarks.getCheckTime()==null&&storeRemarks.getStoreCode().equals(storeDB.getStoreCode())){
                                StoreRemarks storeRemarksDB = storeRemarksService.findById(storeRemarks.getSrId());
                                storeRemarksDB.setCheckMan(userName);
                                storeRemarksDB.setCheckTime(new Date());
                                int change = storeRemarksDB.getNownum()-storeRemarksDB.getOrignnum();
                                storeRemarks.setChangenum(change);
                                storeRemarksService.save(storeRemarksDB);

                            }
                        }
                        itemsService.save(itemsDemo);



                    }
                }
            }
        }
        if(widthBandSet.size()!=0){
            for (WidthBand widthBandJSON:widthBandSet) {
                for(WidthBand widthBandDB:storeDB.getWidthBandSet()){
                    if(widthBandJSON.getWid()==widthBandDB.getWid()){
                        WidthBand widthBandDemo = widthBandService.findById(widthBandDB.getWid());
                        widthBandDemo.setServicePerson(widthBandJSON.getServicePerson());
                        widthBandDemo.setAccessMethod(widthBandJSON.getAccessMethod());
                        widthBandDemo.setPayMethod(widthBandJSON.getPayMethod());
                        widthBandDemo.setPayMoney(widthBandJSON.getPayMoney());
                        widthBandDemo.setIdentity(widthBandJSON.getIdentity());
                        widthBandDemo.setPassword(widthBandJSON.getPassword());
                        widthBandDemo.setTapewidth(widthBandJSON.getTapewidth());
                        widthBandDemo.setEndDate(widthBandJSON.getEndDate());
                       // System.out.println("sign---->"+widthBandJSON.getSign());
                        widthBandDemo.setSign(widthBandJSON.getSign());
                        widthBandDemo.setChenckPerson(userName);
                        widthBandService.save(widthBandDemo);

                    }
                }
            }
        }

        List<String> signList = new ArrayList<>();
        for (Items setItems:itemsSetJSON) {
            signList.add(setItems.getSign()); //设备是否需要更改的标识
        }
        for(WidthBand widthBand:widthBandSet){
            signList.add(widthBand.getSign());
        }
        //包含2说明需要市场IT调整
        if(signList.contains("2")){
            storeDB.setStoreStatus(storeStatusService.findById(3));  //状态待调整
            MailList mailList = new MailList();
            mailList.setMarketName(storeDB.getMarketName());
            mailList.setStoreCode(storeDB.getStoreCode());
            mailList.setStoreName(storeDB.getStoreName());
            mailList.setMailStatus(3);
            mailList.setStoreStatus("待调整");
            mailListSerivice.save(mailList);

        }else{
            storeDB.setStoreStatus(storeStatusService.findById(4));  //状态为已确认
            MailList mailList = new MailList();
            mailList.setMarketName(storeDB.getMarketName());
            mailList.setStoreCode(storeDB.getStoreCode());
            mailList.setStoreName(storeDB.getStoreName());
            mailList.setMailStatus(4);
            mailList.setStoreStatus("已确认");
            mailListSerivice.save(mailList);
        }
      //  storeDB.setItemsSet(jsonStore.getItemsSet());

        storeService.save(storeDB);
        //调整设备时或者已经确认设备时，均需要改变设备的状态

//        Store store1 = storeService.findById(storeDB.getStoreId());
//        if(store1.getItemsSet().size()!=0&&store1.getStoreStatus().getStatusName().equals("已确认")){
//            int index = 0;
//            for(Items items : store1.getItemsSet()){
//                if(items.getSign().equals("1")){
//                   // System.out.println("true");
//                    index++;
//                }
//                //System.out.println("index----->"+index);
//                //System.out.println(store1.getItemsSet().size());
//            }
//            if(index==store1.getItemsSet().size()){
//                for(Items items : store1.getItemsSet()){
//                    //################### 门店日志
//                    StoreRemarks storeRemarks = new StoreRemarks();
//                    storeRemarks.setStoreName(store1.getStoreName());
//                    storeRemarks.setStoreCode(store1.getStoreCode());
//                    storeRemarks.setMarketName(store1.getMarketName());
//                    storeRemarks.setItemName(items.getItem().getName());
//                    storeRemarks.setItemId(items.getItem().getItemId());
//                    storeRemarks.setEquipName(items.getEquipName());
//                    //提交人
//                    storeRemarks.setOperatePerson(items.getOpreationPerson());
//                    //设置操作记录的时间
//                    storeRemarks.setUpdateTime(items.getUpDateTime());
//                    //审批人
//                    storeRemarks.setCheckMan(userName);
//                    storeRemarks.setCheckTime(new Date());
//
//                    //设备原来的数量为0
//                    storeRemarks.setOrignnum(0);
//                    //设备现在的数量
//                    storeRemarks.setNownum(items.getNum());
//                    //设备变化量
//                    storeRemarks.setChangenum(items.getNum()-0);
//                    storeRemarks.setStoreAnditem(store1.getStoreName()+""+items.getItem().getName());
//                    storeRemarksService.save(storeRemarks);
////                      //#####################################
//                }
//
//            }
//        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code","1");
        return map;
    }

    /**
     * 待调整html页面
     * @return
     */
    @RequestMapping("/adjustment")
    public String adjustment(){
        return "/store/adjustore";
    }
    /**
     * 待调整页面的返回按钮
     */
    @RequestMapping("/adjustBtn")
    @ResponseBody
    public Map<String,Object> adjustBtn(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","true");
        return map;
    }



    /**
     * 待调整页面填充
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/adjustList")
    @ResponseBody
    public Page<Store> adjustList(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return storeService.adjustList(page,pageSize,searchName);
    }

    /**
     * 门店宽带信息展示填充页面,进行判断是否需要发送给邮件
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/widthList")
    @ResponseBody
    public Page<Store> widthList(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        Page<Store> storePage = storeService.storeWidthList(page,pageSize,searchName);
        return storePage;
    }
    @RequestMapping("/storeClose")
    @ResponseBody
    public Map<String,Object> storeClose(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        return  map;
    }

    /**
     * 门店关闭页面的具体展示
     * @return
     */
    @RequestMapping("/storeClosePage")
    public String storeClosePage(){
        return "/store/closestore";
    }

    /**
     * 门店闭店页面的List列表展示页面,其中门店的闭店标志为 “null”.
     * 闭店标志位“1”，表示已经闭店
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/storeCloseList")
    @ResponseBody
    public Page<Store> storeCloseList(int page,int pageSize,String searchName){
        return storeService.storeMsgList(page,pageSize,searchName);
    }

    /**
     * 门店闭店列表
     * @param modelAndView
     * @param id
     * @return
     */
    @RequestMapping("/storeCloseItem")
    public ModelAndView storeCloseItem(ModelAndView modelAndView,int id){
        modelAndView.setViewName("/store/storemsg");
        modelAndView.addObject("storeId",id);
        return modelAndView;
    }

    /**
     * 门店修改基本信息时进行保存
     * @param store
     * @return
     */
    @RequestMapping("/storeMsgUpdate")
    public String storeMsgUpdate(Store store){
        Store storeDB = storeService.findById(store.getStoreId());
        storeDB.setStoreCode(store.getStoreCode());
        storeDB.setStoreName(store.getStoreName());
        storeDB.setAddress(store.getAddress());
        storeDB.setMarketName(store.getMarketName());
        storeDB.setMarger(store.getMarger());
        storeDB.setOpenDate(store.getOpenDate());
        storeService.save(storeDB);
       /* System.out.println("修改数据库中的对象----》"+storeDB);*/
        return "redirect:/store/storeClosePage";
    }
    /**
     * 执行闭店操作:
     * 市场IT执行闭店操作-----》管理员进行审批
     * step1:需要改变餐厅的状态为---》5
     * step2:申请闭店的时间
     *
     */
    @RequestMapping("/toExeStoreClose")
    @ResponseBody
    public Map<String,Object> toExeStoreClose(String ids){
        Map<String,Object> map = new HashMap<>();
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        //System.out.println("ids------>"+ids);
        String[] splitId = ids.split(",");
        for (int i = 0; i <splitId.length ; i++) {
            int storeId = Integer.valueOf(splitId[i]);
            Store store = storeService.findById(storeId);
            //设置闭店日期
            store.setCloseDate(new Date());
            //闭店操作人
            store.setClosePerson(userName);
            //闭店的状态转化为5
            StoreStatus storeStatus = storeStatusService.findById(5);  //闭店操作，需要管理员进行审批
            store.setStoreStatus(storeStatus);
            storeService.save(store);
        }
        map.put("code","1");
        return map;
    }

    /**
     * 管理员看到到闭店审批页面
     */
    @RequestMapping("/closecheckList")
    public String closecheckList(){
        return "/store/closecheckList";
    }
    /**
     * 管理员闭店页面的数据填充List页面
     */
    @RequestMapping("/closeStoreList")
    @ResponseBody
    public Page<Store> closeStoreList(int page,int pageSize,String searchName){
        return storeService.closeStoreList(page,pageSize,searchName);
    }

    /**
     * 具体闭店的操作，门店基本信息页面
     * @param modelAndView
     * @param id
     * @return
     */
    @RequestMapping("/closepage")
    public ModelAndView closePage(ModelAndView modelAndView,int id){
        //System.out.println("id------>"+id);
        modelAndView.setViewName("/store/closecontent");
        modelAndView.addObject("storeId",id);
        return  modelAndView;
    }
    /**
     * 管理员提交具体的审批结果
     */
    @RequestMapping("/passCloseStore")
    @ResponseBody
    public Map<String,Object> passCloseStore(int storeId){
        Map<String,Object> map = new HashMap<>();
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        Store store = storeService.findById(storeId);
        //闭店标记
        store.setCloseSign("1");
        //闭店审批人
        store.setCheckPerson(userName);
        //闭店日期
        store.setCheckDate(new Date());
        //门店闭店后，此时状态转化为待添加状态1
        store.setStoreStatus(storeStatusService.findById(1));
        //step1:将设备数量清零，记录在日志表中
        if(store.getItemsSet()!=null){
            Set<Items> itemsSet = store.getItemsSet();
            for(Items items : itemsSet){
                StoreRemarks storeRemarks = new StoreRemarks();
                storeRemarks.setStoreName(store.getStoreName());
                storeRemarks.setStoreCode(store.getStoreCode());
                storeRemarks.setMarketName(store.getMarketName());
                storeRemarks.setItemName(items.getItem().getName());
                storeRemarks.setItemId(items.getItem().getItemId());
                storeRemarks.setOperatePerson(store.getClosePerson());
                storeRemarks.setUpdateTime(store.getCloseDate());
                storeRemarks.setOrignnum(items.getNum());
                storeRemarks.setNownum(0);
                storeRemarks.setChangenum(items.getNum()-0);
                storeRemarks.setCheckMan(userName);
                storeRemarks.setCheckTime(new Date());
                storeRemarks.setStoreAnditem(store.getStoreName()+""+items.getItem().getName());
                storeRemarksService.save(storeRemarks);

                //删除相应的记录
                itemsService.deleteById(items.getId());

            }



            store.setItemsSet(null);
        }
        //step2:将宽带的记录清0
        if(store.getWidthBandSet()!=null){

            //deleteById
            for(WidthBand widthBand : store.getWidthBandSet()){
                //删除相应的宽带设备的id
                widthBandService.deleteById(widthBand.getWid());
            }
            store.setWidthBandSet(null);
        }

        storeService.save(store);

        map.put("code","true");
        return map;
    }



    /**
     * 门店重新开业页面
     * @return
     */
    @RequestMapping("/reStartPage")
    public String reStartPage(){
        return "/store/restartstore";
    }

    /**
     * 重新开业的页面数据
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/reStartList")
    @ResponseBody
    public Page<Store> reStartList(int page,int pageSize,String searchName){
        return storeService.storeRestartList(page,pageSize,searchName);
    }
    /**
     * 执行重新开店的按钮，在数据库中进行执行(执行重新开店操作)
     */
    @RequestMapping("/toExeStoreStart")
    @ResponseBody
    public Map<String,Object> toExeStoreStart(String ids){
        Map<String,Object> map = new HashMap<>();
        String[] splitid = ids.split(",");
        for (int i = 0; i <splitid.length ; i++) {
            int storeId = Integer.valueOf(splitid[i]);
            Store store =  storeService.findById(storeId);
            //闭店标记表示设置为null，
            // 闭店时间设置为null
            // 闭店人设置为null
            store.setCloseSign(null);
            store.setCloseDate(null);
            store.setClosePerson(null);
            //审批过程设置为null
            store.setCheckDate(null);
            store.setCheckPerson(null);

            //门店此时的状态为待选择状态
            StoreStatus storeStatus = storeStatusService.findById(1);
            store.setStoreStatus(storeStatus);
            storeService.save(store);
        }
        map.put("code","1");
        return  map;
    }

    /**
     * 对已经添加设备的门店，且门店的设备已经通过审核。
     *  然后，门店需要添加新的设备时的展示页面
     * @return
     */
    @RequestMapping("/searchInforPage")
    public String searchInforPage(){
        return "/store/searchpageList";
    }

    /**
     *返回页面的按钮展示
     */
    @RequestMapping("/searchBtn")
    @ResponseBody
    public Map<String,Object> searchBtn(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","true");
        return map;
    }


    /**
     * 查看页面展示列表
     */
    @RequestMapping("/marketCheckList")
    @ResponseBody
    public Page<Store> marketCheckList(int page,int pageSize,String searchName){
        return  storeService.marketCheckList(page,pageSize,searchName);
    }
    /**
     * 门店信息的具体展示页面是，storeInfor
     * 市场IT看到的已经审批的门店列表
     * 对于已经被管理员审批过的门店设备的信息，市场IT仍然可以修改，但是需要记录修改的历史
     *
     */
    @RequestMapping("/searchObject")
    public ModelAndView searchObject(ModelAndView modelAndView,int id){
        modelAndView.setViewName("/store/storeInfor");
        modelAndView.addObject("storeId",id);
        return modelAndView;
    }
    /**
     * 点击确认按钮，对门店的设备进行确认之后，此时的状态改为待审批状态
     * @param store
     * @return
     */
    @RequestMapping("/checkStore")
    @ResponseBody
    public Map<String,Object> checkStore(String store){
        Map<String,Object> map = new HashMap<>();
        //此时门店信息修改的主要是门店的宽带信息以及设备信息
        Store storeJSON = JSON.parseObject(store,Store.class);
        Store storeDB = storeService.findById(storeJSON.getStoreId());

        //添加宽带
        if(storeDB.getWidthBandSet().size()!=0){
            for (WidthBand widthBandJSON:storeJSON.getWidthBandSet()) {
               for(WidthBand widthBandDB:storeDB.getWidthBandSet()){
                   if(widthBandJSON.getWid()==widthBandDB.getWid()){
                       WidthBand widthBandDemo = widthBandService.findById(widthBandDB.getWid());
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
            storeDB.setWidthBandSet(storeJSON.getWidthBandSet());
        }

        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        //添加门店的设备信息
        if(storeDB.getItemsSet().size()!=0){
            for(Items itemsJSON:storeJSON.getItemsSet()){
                for(Items itemsDB:storeDB.getItemsSet()){
                    if(itemsJSON.getId()==itemsDB.getId()){
                         Items itemsDemo = itemsService.findById(itemsDB.getId());
                         itemsDemo.setClassName(itemsJSON.getClassName());
                         itemsDemo.setEquipName(itemsJSON.getEquipName());
                         itemsDemo.setNum(itemsJSON.getNum());
                         itemsDemo.setItem(itemsJSON.getItem());
                         itemsDemo.setOpreationPerson(userName);
                         itemsDemo.setUpDateTime(new Date());
                         //待审核
                         itemsDemo.setSign("2");
                        //################### 门店日志
                        StoreRemarks storeRemarks = new StoreRemarks();
                        storeRemarks.setStoreName(storeDB.getStoreName());
                        storeRemarks.setStoreCode(storeDB.getStoreCode());
                        storeRemarks.setMarketName(storeDB.getMarketName());
                        storeRemarks.setItemName(itemsDemo.getItem().getName());
                        storeRemarks.setItemId(itemsDemo.getItem().getItemId());
                        //操作人
                        storeRemarks.setOperatePerson(userName);
                        //设备原来的数量为0
                        storeRemarks.setOrignnum(0);
                        //设备现在的数量
                        storeRemarks.setNownum(itemsJSON.getNum());
                        //设备变化量
                        storeRemarks.setChangenum(itemsJSON.getNum()-0);
                        storeRemarks.setStoreAnditem(storeDB.getStoreName()+""+itemsDemo.getItem().getName());
                        //设置操作记录的时间
                        storeRemarks.setUpdateTime(new Date());
                        storeRemarksService.save(storeRemarks);
                        itemsService.save(itemsDemo);

                    }
                }
            }
            storeDB.setItemsSet(storeJSON.getItemsSet());
        }
        storeDB.setStoreStatus(storeStatusService.findById(2));

        //只要添加设备默认状态为2，需要管理人审批(不覆盖原来的记录)
        MailList mailList = new MailList();
        mailList.setMailStatus(2);
        mailList.setStoreStatus("待审批");
        mailList.setMarketName(storeDB.getMarketName());
        mailList.setStoreCode(storeDB.getStoreCode());
        mailList.setStoreName(storeDB.getStoreName());
        mailListSerivice.save(mailList);
        storeService.save(storeDB);
        map.put("code","1");
        return  map;
    }

    /**
     * 当门店宽带审批通过后，再次添加门店的宽带信息
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
        modelAndView.setViewName("/store/storeInfor");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;
    }
    /**
     * 门店的设备点击确定后，再次添加门店的设备信息
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
        items.setOpreationPerson(userName);
        items.setUpDateTime(new Date());


        itemsService.save(items);

        Set<Items> itemsSet = store.getItemsSet();
        itemsSet.add(items);
        store.setItemsSet(itemsSet);


        storeService.save(store);
        modelAndView.setViewName("/store/storeInfor");
        modelAndView.addObject("storeId",storeId);
        return modelAndView;
    }

    /**
     * 当管理员确定门店的宽带和设备信息之后，
     * 市场IT对审核过设备的更改需要记录修改的记录
     * (1).记录修改的记录
     * (2).此时的门店状态调整为待审批状态
     */
    @RequestMapping("/addMsgStore")
    @ResponseBody
    public Map<String,Object> addMsgStore(String store){
       System.out.println("store----->"+store);

        Store jsonStore = JSON.parseObject(store,Store.class);

        System.out.println("jsonStore---->"+jsonStore);

        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        Set<Items> itemsSetJSON = jsonStore.getItemsSet();
        Set<WidthBand> widthBandSet = jsonStore.getWidthBandSet();
        Store storeDB = storeService.findById(jsonStore.getStoreId());
        //待审批的状态
        StoreStatus storeStatus = storeStatusService.findById(2);

        if(storeDB.getItemsSet().size()!=0){
            for (Items itemsDB:storeDB.getItemsSet()) {
                for (Items itemsJSON:itemsSetJSON) {
                    //修改原来的设备信息
                    if(itemsDB.getId()==itemsJSON.getId()){
                        Items itemsDemo = itemsService.findById(itemsJSON.getId());
                        itemsDemo.setClassName(itemsJSON.getClassName());
                        itemsDemo.setEquipName(itemsJSON.getEquipName());
                        itemsDemo.setItem(itemsJSON.getItem());
                        //说明此时市场IT修改了门店设备信息
                        if(itemsJSON.getNum()!=itemsDemo.getNum()){
                            if(itemsDemo.getSign().equals("1")){
                                StoreRemarks storeRemarks = new StoreRemarks();
                                storeRemarks.setStoreName(storeDB.getStoreName());
                                storeRemarks.setStoreCode(storeDB.getStoreCode());
                                storeRemarks.setMarketName(storeDB.getMarketName());
                                storeRemarks.setItemName(itemsJSON.getItem().getName());
                                storeRemarks.setItemId(itemsJSON.getItem().getItemId());
                                //操作人(修改人)
                                storeRemarks.setOperatePerson(userName);
                                storeRemarks.setUpdateTime(new Date());
                                storeRemarks.setOrignnum(itemsDemo.getNum());
                                storeRemarks.setNownum(itemsJSON.getNum());
                                int change = itemsJSON.getNum() - itemsDemo.getNum();
                                storeRemarks.setChangenum(change);
                                storeRemarks.setStoreAnditem(storeDB.getStoreName()+""+itemsDemo.getItem().getName());
                                storeRemarksService.save(storeRemarks);

                                //此时有需要管理员进行审批
                                itemsDemo.setSign("2");
                                itemsDemo.setNum(itemsJSON.getNum());
                                itemsService.save(itemsDemo);

                                storeDB.setStoreStatus(storeStatus);
                            }

                        }else{
                            if(itemsDemo.getSign().equals("2")){
                                itemsDemo.setNum(itemsJSON.getNum());
                                itemsService.save(itemsDemo);
                                storeDB.setStoreStatus(storeStatus);
                            }

//                            if(itemsDemo.getSign()==null){
//                                itemsDemo.setSign("2");
//
//                            }

                        }

                    }

                }
            }
          //  storeDB.setItemsSet(jsonStore.getItemsSet());
        }
        if(storeDB.getWidthBandSet().size()!=0){
            for(WidthBand widthBandDB:storeDB.getWidthBandSet()){
                for(WidthBand widthBandJSON : widthBandSet){
                    if(widthBandDB.getWid()==widthBandJSON.getWid()){
                        WidthBand widthBandDemo = widthBandService.findById(widthBandDB.getWid());
                        if(widthBandDemo.getSign().equals("1")){
                            //对比通过审核的设备是否又新的变化
                            //设置两个新的WidthCheck
                            WidthCheck widthCheck1 = new WidthCheck();
                            widthCheck1.setIdentity(widthBandDemo.getIdentity());
                            widthCheck1.setPassword(widthBandDemo.getPassword());
                            widthCheck1.setPayMoney(widthBandDemo.getPayMoney());
                            widthCheck1.setTapewidth(widthBandDemo.getTapewidth());
                            widthCheck1.setEndDate(String.valueOf(widthBandDemo.getEndDate()));
                            System.out.println("demo---->"+String.valueOf(widthBandDemo.getEndDate()));

                            WidthCheck widthCheck2 = new WidthCheck();
                            widthCheck2.setIdentity(widthBandJSON.getIdentity());
                            widthCheck2.setPassword(widthBandJSON.getPassword());
                            widthCheck2.setPayMoney(widthBandJSON.getPayMoney());
                            widthCheck2.setTapewidth(widthBandJSON.getTapewidth());

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String format = simpleDateFormat.format(widthBandJSON.getEndDate());
                            widthCheck2.setEndDate(format);
                            System.out.println("jsonDate----->"+format);


                            CompareProperties compareProperties  = new CompareProperties();
                            boolean result = compareProperties.contrastObj(widthCheck1, widthCheck2);
                            System.out.println("result----->"+result);

                            if(widthBandDemo.getServicePerson().getSid()!=widthBandJSON.getServicePerson().getSid()||
                                    widthBandDemo.getAccessMethod().getAid()!=widthBandJSON.getAccessMethod().getAid()||
                                    widthBandDemo.getPayMethod().getPayid()!=widthBandJSON.getPayMethod().getPayid()||!result){
                                widthBandDemo.setServicePerson(widthBandJSON.getServicePerson());
                                widthBandDemo.setAccessMethod(widthBandJSON.getAccessMethod());
                                widthBandDemo.setPayMethod(widthBandJSON.getPayMethod());
                                widthBandDemo.setPayMoney(widthBandJSON.getPayMoney());
                                widthBandDemo.setIdentity(widthBandJSON.getIdentity());
                                widthBandDemo.setPassword(widthBandJSON.getPassword());
                                widthBandDemo.setTapewidth(widthBandJSON.getTapewidth());
                                widthBandDemo.setEndDate(widthBandJSON.getEndDate());
                                widthBandDemo.setSign("2");
                                storeDB.setStoreStatus(storeStatus);
                                widthBandService.save(widthBandDemo);

                            }
                        }else{
                            widthBandDemo.setServicePerson(widthBandJSON.getServicePerson());
                            widthBandDemo.setAccessMethod(widthBandJSON.getAccessMethod());
                            widthBandDemo.setPayMethod(widthBandJSON.getPayMethod());
                            widthBandDemo.setPayMoney(widthBandJSON.getPayMoney());
                            widthBandDemo.setIdentity(widthBandJSON.getIdentity());
                            widthBandDemo.setPassword(widthBandJSON.getPassword());
                            widthBandDemo.setTapewidth(widthBandJSON.getTapewidth());
                            widthBandDemo.setEndDate(widthBandJSON.getEndDate());
                            widthBandDemo.setSign("2");
                            storeDB.setStoreStatus(storeStatus);
                            widthBandService.save(widthBandDemo);

                        }

                    }
                }
            }
           // storeDB.setWidthBandSet(jsonStore.getWidthBandSet());
        }
        storeService.save(storeDB);
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        return map;
    }
    /**
     * 测试末页分页效果
     */
    @RequestMapping("/findAllTest")
    @ResponseBody
    public Map<String,Object> findAllTest(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        Map<String,Object> map = new HashMap<>();
        Page<Store> all = storeService.findAll(page, pageSize, searchName);
        map.put("pageTotals",all.getTotalPages());
        return  map;
    }
    //adjustList
    @RequestMapping("/adjustListTest")
    @ResponseBody
    public Map<String,Object> adjustListTest(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        Map<String,Object> map = new HashMap<>();
        Page<Store> storePage = storeService.adjustList(page, pageSize, searchName);
        map.put("pageTotals",storePage.getTotalPages());
        return map;
    }
    @RequestMapping("/storeCloseListTest")
    @ResponseBody
    public Map<String,Object> storeCloseListTest(int page,int pageSize,String searchName){
        Map<String,Object> map = new HashMap<>();
        Page<Store> storePage = storeService.storeMsgList(page, pageSize, searchName);
        map.put("pageTotals",storePage.getTotalPages());
        return map;
    }

    /**
     * 门店移除设备信息,并且会删除相应的日志里的记录
     * @param itemsId
     * @return
     */
    @RequestMapping("/deleteByItemsId")
    @ResponseBody
    public Map<String,Object> deleteByItemsId(int itemsId){
        Map<String,Object> map = new HashMap<>();
        int itemId = itemsService.findById(itemsId).getItem().getItemId();
        itemsService.deleteById(itemsId);
       // storeRemarksService.deleteById(storeRemarksService.findByItemsId(itemId).getSrId());
        map.put("code","true");
        return map;
    }

    /**
     * content页面的保存按钮
     */
    @RequestMapping("/saveStoreInfor")
    @ResponseBody
    public Map<String,Object> saveStoreInfor(String store){
        Map<String,Object> map = new HashMap<>();
        //此时门店信息修改的主要是门店的宽带信息以及设备信息
        Store storeJSON = JSON.parseObject(store,Store.class);
        Store storeDB = storeService.findById(storeJSON.getStoreId());
        //添加宽带
        if(storeDB.getWidthBandSet().size()!=0){
            for (WidthBand widthBandJSON:storeJSON.getWidthBandSet()) {
                for(WidthBand widthBandDB:storeDB.getWidthBandSet()){
                    if(widthBandJSON.getWid()==widthBandDB.getWid()){
                        WidthBand widthBandDemo = widthBandService.findById(widthBandDB.getWid());
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
            storeDB.setWidthBandSet(storeJSON.getWidthBandSet());
        }
        //添加门店的设备信息
        if(storeDB.getItemsSet().size()!=0){
            for(Items itemsJSON:storeJSON.getItemsSet()){
                for(Items itemsDB:storeDB.getItemsSet()){
                    if(itemsJSON.getId()==itemsDB.getId()){
                        Items itemsDemo = itemsService.findById(itemsDB.getId());
                        itemsDemo.setClassName(itemsJSON.getClassName());
                        itemsDemo.setEquipName(itemsJSON.getEquipName());
                        itemsDemo.setNum(itemsJSON.getNum());
                        itemsDemo.setItem(itemsJSON.getItem());
                        //待审核
                        itemsDemo.setSign("2");
                        itemsService.save(itemsDemo);
                    }
                }
            }
            storeDB.setItemsSet(storeJSON.getItemsSet());
        }
        storeService.save(storeDB);
        map.put("code","true");
        return map;
    }
    /**
     * 管理员看到的已确认门店的页面
     */
    @RequestMapping("/storeAdminPage")
    public String storeAdminPage(){
        return "/store/storeAdminpage";
    }
    /**
     * 管理员点击查看按钮进行撤回审批的页面
     */
    @RequestMapping("/storeStatusBack")
    public ModelAndView storeStatusBack(ModelAndView modelAndView,int id){
        modelAndView.setViewName("/store/storestatusback");
        modelAndView.addObject("storeId",id);
        return modelAndView;
    }
    /**
     * 管理员撤回审批，
     * 此时的门店的状态由4已经确认状态，流转到待审批状态
     */
    @RequestMapping("/back")
    @ResponseBody
    public Map<String,Object> bank(String store){
        Map<String,Object> map = new HashMap<>();
        //此时门店信息修改的主要是门店的宽带信息以及设备信息
        Store storeJSON = JSON.parseObject(store,Store.class);
        Store storeDB = storeService.findById(storeJSON.getStoreId());

        //添加宽带
        if(storeDB.getWidthBandSet().size()!=0){
            for (WidthBand widthBandJSON:storeJSON.getWidthBandSet()) {
                for(WidthBand widthBandDB:storeDB.getWidthBandSet()){
                    if(widthBandJSON.getWid()==widthBandDB.getWid()){
                        WidthBand widthBandDemo = widthBandService.findById(widthBandDB.getWid());
                        if(!widthBandJSON.getSign().equals(widthBandDemo.getSign())){
                            widthBandDemo.setSign(widthBandJSON.getSign());
                            widthBandDemo.setChenckPerson(null);
                        }
                        widthBandService.save(widthBandDemo);
                    }
                }
            }
            storeDB.setWidthBandSet(storeJSON.getWidthBandSet());
        }
        //添加门店的设备信息
        if(storeDB.getItemsSet().size()!=0){
            for(Items itemsJSON:storeJSON.getItemsSet()){
                for(Items itemsDB:storeDB.getItemsSet()){
                    if(itemsJSON.getId()==itemsDB.getId()){
                        Items itemsDemo = itemsService.findById(itemsDB.getId());
                        if(!itemsJSON.getSign().equals(itemsDemo.getSign())){
                            itemsDemo.setSign(itemsJSON.getSign());
                            itemsDemo.setCheckPerson(null);
                        }
                        itemsService.save(itemsDemo);

                    }
                }
            }
            storeDB.setItemsSet(storeJSON.getItemsSet());
        }
        //撤回后门店的状态为待审核状态
        storeDB.setStoreStatus(storeStatusService.findById(2));
        storeService.save(storeDB);
        map.put("code","true");
        return map;
    }
    /**
     * 通过餐厅编码，找到门店
     */
    @RequestMapping("/findByStoreCode")
    @ResponseBody
    public Map<String,Object> findByStoreCode(String storeCode){
        Map<String,Object> map = new HashMap<>();
        if(storeService.findByStoreCode(storeCode)!=null){
            map.put("code","true");
        }else{
            map.put("code","false");
        }
        return  map;

    }




















}
