package com.xiabuxiabu.storemanage.controller.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.*;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.DateTool;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.store.*;
import com.xiabuxiabu.storemanage.service.user.UserService;
import net.bytebuddy.asm.Advice;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/store")
/*@EnableConfigurationProperties(EMailProperties.class)*/
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
    private DateTool dateTool;
    @Autowired
    private HttpServletRequest httpServletRequest;  //拿到sesssion中得登录人
    @Autowired
    private UserService userService;
    @Autowired
    private EMailProperties eMailProperties;
    @Autowired
    private EMailTask eMailTask;
    @Autowired
    private MailListSerivice mailListSerivice;

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
        //若已经又宽带信息
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
        Store store  =  storeService.findById(storeId);

        //设置门店的展示样式，再根据门店的基本信息进行填充
        if(store.getItemsSet().size()!=0){
            Set<Items> oldItmsSet = store.getItemsSet();
            //对于新添加的设备，默认状态为待审核状态
            items.setSign("2");
            Items saveItems = itemsService.save(items);
            oldItmsSet.add(saveItems);
            store.setItemsSet(oldItmsSet);
            storeService.save(store);
        }else{
            //新添加的设别，默认都需要进行验证
            items.setSign("2");
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
     * @param store
     * @return
     */
    @RequestMapping("/toupdate")
    @ResponseBody
    public Map<String,Object> toupdate(String store){
        //转换为json对象
        Store jsonStore  = JSON.parseObject(store,Store.class);
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        Set<Items> itemsSetJSON = jsonStore.getItemsSet();
        Store storeDB = storeService.findById(jsonStore.getStoreId());
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
                        itemsService.save(itemsDemo);
                    }
                }
            }
        }
        List<String> signList = new ArrayList<>();
        for (Items setItems:itemsSetJSON) {
            signList.add(setItems.getSign()); //设备是否需要更改的标识
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
        storeDB.setItemsSet(jsonStore.getItemsSet());
        storeService.save(storeDB);
        //调整设备时或者已经确认设备时，均需要改变设备的状态

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
        List<Store> storeList = storePage.getContent();
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        //拿到当前登录人(user)
        User user = userService.findByUserName(userName);
//        for (int i = 0; i <storeList.size() ; i++) {
//             Store store = storeList.get(i);
//
//             Date widthEndDate = store.getWidthBand().getEndDate();
//             SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//             StringBuffer content = new StringBuffer();
//            try {
//                Date nowDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
//                int lastDays = dateTool.differentDaysByMillisecond(widthEndDate,nowDate);
//                // System.out.println("时间----》"+lastDays);
//                content.append("<html><head></head>");
//                content.append("<body><div><h2>宽带到期通知</h2>" +
//                        "亲爱的用户:您好!门店："+store.getStoreName()+"("+store.getStoreCode()+")的宽带还有"+lastDays+"天即将到期。请您及时进行处理，" +
//                        "如已处理，请忽略。谢谢！</div>");
//                content.append("<div><span style ='float: right;'>总部资讯</span></div>");
//                content.append("</body></html>");
//                //根据时间差，来发送邮件;宽带付款方式为年付
//                //当时间为一个月前，一天通知一次
//                //先暂停发送邮件的操作
//                if(lastDays>30){
//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            String [] sendAll = {user.getMail()};
//                            try {
//                                eMailTask.sendHtmlMail(eMailProperties.getNickname(),sendAll,eMailProperties.getSubject(),content.toString(),eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    ScheduledExecutorService service = Executors
//                            .newSingleThreadScheduledExecutor();
//                    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
//                    service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.DAYS);
//                }else if(lastDays>15&&lastDays<=30){
//                    //时间段为15-30天时，一天通知两次
//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            String [] sendAll = {user.getMail()};
//                            try {
//                                eMailTask.sendHtmlMail(eMailProperties.getNickname(),sendAll,eMailProperties.getSubject(),content.toString(),eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    ScheduledExecutorService service = Executors
//                            .newSingleThreadScheduledExecutor();
//                    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
//                    service.scheduleAtFixedRate(runnable, 0, 12, TimeUnit.HOURS);
//                }else if (lastDays>0&&lastDays<=15){
//                    //0-15天时，一天通知三次
//                    Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            String [] sendAll = {user.getMail()};
//                            try {
//                                eMailTask.sendHtmlMail(eMailProperties.getNickname(),sendAll,eMailProperties.getSubject(),content.toString(),eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    ScheduledExecutorService service = Executors
//                            .newSingleThreadScheduledExecutor();
//                    // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
//                    service.scheduleAtFixedRate(runnable, 0, 8, TimeUnit.HOURS);
//                }else if(lastDays==0){
//                    long yearAfter = (widthEndDate.getTime()/1000)+60*60*24*365;
//                    widthEndDate.setTime(yearAfter*1000);
//                    String yearAfterStr = simpleDateFormat.format(widthEndDate);
//                    Date parse = simpleDateFormat.parse(yearAfterStr);
//                    System.out.println("一年以后的日期----》"+parse);
//                    store.getWidthBand().setEndDate(parse);
//                    storeService.save(store);
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
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
     * 执行闭店操作
     * step1:首先将门店的设备请0
     * step2:记录在历史表中，可以查询
     */
    @RequestMapping("/toExeStoreClose")
    @ResponseBody
    public Map<String,Object> toExeStoreClose(String ids){
        Map<String,Object> map = new HashMap<>();
        System.out.println("ids------>"+ids);
        String[] splitId = ids.split(",");
        for (int i = 0; i <splitId.length ; i++) {
            int storeId = Integer.valueOf(splitId[i]);
            System.out.println("storeId----->"+storeId);
            Store store = storeService.findById(storeId);
            //设置闭店日期
            store.setCloseDate(new Date());
            //闭店时设置闭店标志
            store.setCloseSign("1");
            //闭店后此时的餐厅状态为待选择状态
            StoreStatus storeStatus = storeStatusService.findById(1);
            store.setStoreStatus(storeStatus);
            //设置数量清0
            if(store.getItemsSet()!=null){
                for (Items items:store.getItemsSet()) {
                    Items itemsDB = itemsService.findById(items.getId());
                    //设备清0
                    itemsDB.setNum(0);
                    //记录操作人(且操作人，可以进行查询)
                    itemsDB.setPersonName((String) httpServletRequest.getSession().getAttribute("userName"));
                    //记录操作的时间
                    itemsDB.setUpdateTime(new Date());
                    itemsService.save(itemsDB);
                }
            }
            storeService.save(store);
        }
        map.put("code","1");
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
        //System.out.println("ids----->"+ids);
        Map<String,Object> map = new HashMap<>();
        String[] splitid = ids.split(",");
        for (int i = 0; i <splitid.length ; i++) {
            int storeId = Integer.valueOf(splitid[i]);
            Store store =  storeService.findById(storeId);
            //闭店标记表示设置为null，闭店时间设置为null
            store.setCloseSign(null);
            store.setCloseDate(null);
            //门店的设备Items为null
            store.setItemsSet(null);
            //设置门店的宽带为null
            store.setWidthBandSet(null);
            //门店此时的状态为待选择状态
            StoreStatus storeStatus = storeStatusService.findById(1);
            store.setStoreStatus(storeStatus);
            storeService.save(store);
        }
        map.put("code","1");
        return  map;
    }

    /**
     * 查看设备的按钮事件
     */
    @RequestMapping("/searchInfor")
    @ResponseBody
    public Map<String,Object> searchInfor(){
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        return map;
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
     * 查看页面展示列表
     */
    @RequestMapping("/findByRepostList")
    @ResponseBody
    public Page<Store> findByRepostList(int page,int pageSize,String searchName){
        return  storeService.findByRepostList(page,pageSize,searchName);
    }
    /**
     * 门店信息的具体展示页面是，storeInfor
     */
    @RequestMapping("/searchObject")
    public ModelAndView searchObject(ModelAndView modelAndView,int id){
        modelAndView.setViewName("/store/storeInfor");
        modelAndView.addObject("storeId",id);
        return modelAndView;
    }
    @RequestMapping("/additemInfor")
    public ModelAndView additemInfor(ModelAndView modelAndView, int storeId,int storeStatus,Items items){
        Store store  =  storeService.findById(storeId);
        StoreStatus storeStatusEntity =  storeStatusService.findById(storeStatus);
        store.setStoreStatus(storeStatusEntity);   //设置门店状态为待审核
        //设置门店的展示样式，再根据门店的基本信息进行填充
        if(store.getItemsSet().size()!=0){
            Set<Items> oldItmsSet = store.getItemsSet();
            //对于新添加的设备，默认状态为待审核状态
            items.setSign("2");
            Items saveItems = itemsService.save(items);
            oldItmsSet.add(saveItems);
            store.setItemsSet(oldItmsSet);
            storeService.save(store);
        }else{
            //新添加的设别，默认都需要进行验证
            items.setSign("2");
            Items saveItems =  itemsService.save(items);
            Set<Items> newItemsSet = new HashSet<>();
            newItemsSet.add(saveItems);
            store.setItemsSet(newItemsSet);
            storeService.save(store);
        }
        modelAndView.setViewName("/store/storeInfor");
        modelAndView.addObject("storeId",storeId);
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
        storeDB.setStoreStatus(storeStatusService.findById(2));

        storeService.save(storeDB);
        map.put("code","1");

        //只要添加设备默认状态为2，需要管理人审批(不覆盖原来的记录)
        MailList mailList = new MailList();
        mailList.setMailStatus(2);
        mailList.setStoreStatus("待审批");
        mailList.setMarketName(storeDB.getMarketName());
        mailList.setStoreCode(storeDB.getStoreCode());
        mailList.setStoreName(storeDB.getStoreName());
        mailListSerivice.save(mailList);

        return  map;
    }








}
