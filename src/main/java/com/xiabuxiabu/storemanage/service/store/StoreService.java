package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.entity.store.WidthBand;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.repository.store.StoreRepository;
import com.xiabuxiabu.storemanage.service.user.UserService;
import jdk.net.SocketFlow;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;

    public Page<Store> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Path<String> closeSign = root.get("closeSign");   //获取门店得闭店标志
                    Path<String> band= root.get("band");  //根据门店的品牌进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                    Predicate p4 = criteriaBuilder.like(band,"%"+values+"%");
                    Predicate predicateCloseSign =  criteriaBuilder.isNull(closeSign);
                    Predicate p = criteriaBuilder.or(p1, p2);
                    //拿到登录人的信息
                    //
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();
                    String bandName = userService.findByUserName(userName).getBand();
                    Predicate marketNameSerarch=null;
                    Predicate storeStatusNameWait = null;
                    Predicate bandSearch = null;
                   // Predicate stpreStatusNameChange = null;
                    //按门店状态进行分配
                    Path<StoreStatus> storeStatus = root.get("storeStatus");
                    if(!marketName.equals("总部")){
                        //非管理员操作（门店状态是待选择/待选择）
                        //开始的展示列表事待选择的列表
                         marketNameSerarch = criteriaBuilder.equal(market,marketName);
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"待选择");
                        bandSearch = criteriaBuilder.equal(band,bandName);
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        criteriaQuery.where(p,marketNameSerarch,predicate,predicateCloseSign,bandSearch);

                    }else {
                        //管理员所属的市场为总部,这是管理员可以按市场进行门店的搜索
                        //（门店状态是待审批）
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"待审批");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        p=criteriaBuilder.or(p1, p2, p3,p4);
                        criteriaQuery.where(p,predicate,predicateCloseSign);
                    }
                    return null;
                }
            },pageable);
        }
        return storeRepository.findAll(pageable);
    }
    public Store findById(int id) {
        return storeRepository.findById(id).get();
    }
    public Store save(Store store){
        return  storeRepository.save(store);
    }
    public Store findByStoreCode(String code){
        return storeRepository.findByStoreCode(code);
    }
//    public List<Store> findByMarketName(String marketName){
//        return storeRepository.findByMarketName(marketName);
//    }
    public Page<Store> adjustList(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Path<String> band= root.get("band");  //根据门店的品牌进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                    Predicate p4 = criteriaBuilder.like(band,"%"+values+"%");
                    Predicate p = criteriaBuilder.or(p1, p2);
                    Path<String> closeSign = root.get("closeSign");   //获取门店得闭店标志
                    Predicate predicateCloseSign =  criteriaBuilder.isNull(closeSign);
                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();
                    String bandName = userService.findByUserName(userName).getBand();
                    Predicate marketNameSerarch=null;
                    Predicate storeStatusNameWait = null;
                    Predicate bandSearch = null;
                    // Predicate stpreStatusNameChange = null;
                    //按门店状态进行分配
                    Path<StoreStatus> storeStatus = root.get("storeStatus");
                    if(!marketName.equals("总部")){
                        //非管理员操作（门店状态是待选择/待选择）
                        //开始的展示列表事待选择的列表
                        marketNameSerarch = criteriaBuilder.equal(market,marketName);
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"待调整");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        bandSearch = criteriaBuilder.equal(band,bandName);
                        criteriaQuery.where(p,marketNameSerarch,predicate,predicateCloseSign,bandSearch);
                    }else {
                        //管理员所属的市场为总部,这是管理员可以按市场进行门店的搜索
                        //（门店状态是待审批）
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"已确认");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        p=criteriaBuilder.or(p1, p2, p3,p4);
                        criteriaQuery.where(p,predicate,predicateCloseSign);
                    }
                    return null;
                }
            },pageable);
        }
        return storeRepository.findAll(pageable);
    }
    public List<Store> findReport(){
        return storeRepository.findAll();
    }
    public List<Store> findByStoreStatus(){
        return storeRepository.findByStoreStatus("已确认");
    }

    /**
     * 对导出的excel数据进行分页处理。
     * 根据门店餐厅名称、编码、市场、查询已经确认的门店信息
     * @param page
     * @param size
     * @param values
     * @return
     */
    public Page<Store> findByRepostList(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    //Path<MarketEntity> market = root.get("marketCode");  //根据门店所属市场进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Path<StoreStatus> storeStatus =root.get("storeStatus");  //查询的门店状态
                    Path<String> closeSign = root.get("closeSign");   //获取门店得闭店标志
                    Path<String> band= root.get("band");  //根据门店的品牌进行查询
                    Predicate predicateCloseSign =  criteriaBuilder.isNull(closeSign);
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");  //名称
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");  //编码
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%"); //市场名称
                    Predicate p4 = criteriaBuilder.equal(storeStatus.get("statusName"),"已确认");
                    Predicate p5 = criteriaBuilder.like(band,"%"+values+"%");
                    Predicate p =  criteriaBuilder.or(p1,p2,p3,p5);  //符合查询条件
                    criteriaQuery.where(p,p4,predicateCloseSign);
                    return null;
                }
            },pageable);
        }
        return  storeRepository.findAll(pageable);

    }

    //加载相应的门店宽带信息
    /**
     * 市场IT拿到所属自己市场的门店宽带信息
     * @param page
     * @param size
     * @param values
     * @return
     */
    public Page<Store> storeWidthList(int page,int size,String values){
         //对门店中某一属性进行排序。宽带的到期日期进行升序排序。
         Sort sort = new Sort(Sort.Direction.ASC,"widthBandSet.endDate");
         Pageable pageable = PageRequest.of(page-1,size,sort);

         // Join join = root.join(root.getModel().getSet("itemsSet", Items.class),JoinType.LEFT);

         if(values!=null){
             return  storeRepository.findAll(new Specification<Store>() {
                 @Override
                 public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                     Path<String> name = root.get("storeName");  //餐厅名称进行查询
                     Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                     Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                     Path<String> band= root.get("band");  //根据门店的品牌进行查询
                     Join join= root.join(root.getModel().getSet("widthBandSet", WidthBand.class),JoinType.LEFT);
                     //需要宽带信息不为空
                     Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                     Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                     Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                     Predicate pp1 = criteriaBuilder.like(band,"%"+values+"%");
                     Predicate p = criteriaBuilder.or(p1, p2);
                     Predicate p4 = criteriaBuilder.isNotNull(join.get("endDate"));
                     Path<String> closeSign = root.get("closeSign");   //获取门店得闭店标志
                     Predicate predicateCloseSign =  criteriaBuilder.isNull(closeSign);
                     //拿到登录人的信息
                     String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                     //登陆人的所属市场的信息
                     String marketName = userService.findByUserName(userName).getMarketName();
                     //登录人所属品牌
                     String bandName = userService.findByUserName(userName).getBand();
                     Predicate marketNameSerarch=null;
                     Predicate bandSerach = null;
                     if(!marketName.equals("总部")){
                         //市场IT展示自己所属是市场的门店信息
                         marketNameSerarch = criteriaBuilder.equal(market,marketName);
                         bandSerach = criteriaBuilder.equal(band,bandName);
                         criteriaQuery.where(p,marketNameSerarch,p4,predicateCloseSign,bandSerach);
                     }else {
                         //管理员展示所有的门店信息
                         p=criteriaBuilder.or(p1, p2, p3,pp1);
                         criteriaQuery.where(p,p4,predicateCloseSign);
                     }
                     return null;
                 }
             },pageable);
         }
         return storeRepository.findAll(pageable);
    }

    /**
     * 门店设备历史更新查询
     * @param page
     * @param size
     * @param values
     * @return
     */
    public Page<Store> itemHistory(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
             return storeRepository.findAll(new Specification<Store>() {
                 @Override
                 public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                     Path<String> name = root.get("storeName");  //餐厅名称进行查询
                     Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                   //  Path<MarketEntity> market = root.get("marketCode");  //根据门店所属市场进行查询
                     Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                     Path<String> band= root.get("band");  //根据门店的品牌进行查询

                     Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                     Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                     Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                     Predicate p5 = criteriaBuilder.like(band,"%"+values+"%");
                     Join join = root.join(root.getModel().getSet("itemsSet", Items.class),JoinType.LEFT);
                     Predicate p4 = criteriaBuilder.isNotNull(join.get("personName"));
                     Predicate p = criteriaBuilder.or(p1, p2);
                     Path<String> closeSign = root.get("closeSign");   //获取门店得闭店标志
                     Predicate predicateCloseSign =  criteriaBuilder.isNull(closeSign);
                     //拿到登录人的信息
                     String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                     String marketName = userService.findByUserName(userName).getMarketName();
                     String bandName = userService.findByUserName(userName).getBand();
                     Predicate marketNameSerarch=null;
                     Predicate bandNameSearch = null;
                     if(!marketName.equals("总部")){
                         //市场IT展示自己所属是市场的门店信息
                         marketNameSerarch = criteriaBuilder.equal(market,marketName);
                         bandNameSearch = criteriaBuilder.equal(band,bandName);
                         criteriaQuery.where(p,marketNameSerarch,p4,predicateCloseSign,bandNameSearch);
                     }else {
                         //管理员展示所有的门店信息
                         p=criteriaBuilder.or(p1, p2, p3,p5);
                         criteriaQuery.where(p,p4,predicateCloseSign);
                     }
                     return null;
                 }
             },pageable);
        }
        return  storeRepository.findAll(pageable);
    }

    /**
     * 是否闭店的门店
     * @param page
     * @param size
     * @param values
     * @return
     */
    public Page<Store> storeMsgList(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Path<String> band= root.get("band");  //根据门店的品牌进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                    Predicate p4 = criteriaBuilder.like(band,"%"+values+"%");

                    Predicate p = criteriaBuilder.or(p1, p2);
                    Path<String> closeSign = root.get("closeSign");   //获取门店得闭店标志
                    Predicate predicateCloseSign =  criteriaBuilder.isNull(closeSign);

                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();
                    String bandName = userService.findByUserName(userName).getBand();
                    Predicate marketNameSerarch=null;
                    Predicate bandNameSearch =null;
                    if(!marketName.equals("总部")){
                        //市场IT展示自己所属是市场的门店信息
                        marketNameSerarch = criteriaBuilder.equal(market,marketName);
                        bandNameSearch = criteriaBuilder.equal(band,bandName);
                        criteriaQuery.where(p,marketNameSerarch,predicateCloseSign,bandNameSearch);
                    }else {
                        //管理员展示所有的门店信息
                        p=criteriaBuilder.or(p1, p2, p3,p4);
                        criteriaQuery.where(p,predicateCloseSign);
                    }
                    return null;
                }
            },pageable);
        }
        return storeRepository.findAll(pageable);

    }

    /**
     * 加载数据位准备重新开业的门店
     * @param page
     * @param size
     * @param values
     * @return
     */
    public Page<Store> storeRestartList(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Path<String> band= root.get("band");  //根据门店的品牌进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                    Predicate p4 = criteriaBuilder.like(band,"%"+values+"%");
                    Predicate p = criteriaBuilder.or(p1, p2);
                    //获取门店得闭店标志为“1”的，然后查询出来
                    Path<String> closeSign = root.get("closeSign");
                    Predicate predicateCloseSign =  criteriaBuilder.equal(closeSign,"1");
                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();
                    String bandName = userService.findByUserName(userName).getBand();
                    Predicate marketNameSerarch=null;
                    Predicate bandNameSearch =null;
                    if(!marketName.equals("总部")){
                        //市场IT展示自己所属是市场的门店信息
                        marketNameSerarch = criteriaBuilder.equal(market,marketName);
                        bandNameSearch = criteriaBuilder.equal(band,bandName);
                        criteriaQuery.where(p,marketNameSerarch,predicateCloseSign,bandNameSearch);
                    }else {
                        //管理员展示所有的门店信息
                        p=criteriaBuilder.or(p1, p2, p3,p4);
                        criteriaQuery.where(p,predicateCloseSign);
                    }

                    return null;
                }
            },pageable);
        }
        return  storeRepository.findAll(pageable);
    }

    public List<String> marketNameList(){
        return storeRepository.marketNameList();
    }
    public List<Store> findByMarketName(String marketName){
        return storeRepository.findByMarketName(marketName);
    }








}
