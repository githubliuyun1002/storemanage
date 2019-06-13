package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
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
                    Path<MarketEntity> market = root.get("marketCode");  //根据门店所属市场进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market.get("name"),"%"+values+"%");
                    Predicate p = criteriaBuilder.or(p1, p2);
                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketEntity().getName();
                    Predicate marketNameSerarch=null;
                    Predicate storeStatusNameWait = null;
                   // Predicate stpreStatusNameChange = null;
                    //按门店状态进行分配
                    Path<StoreStatus> storeStatus = root.get("storeStatus");
                    if(!marketName.equals("总部")){
                        //非管理员操作（门店状态是待选择/待选择）
                        //开始的展示列表事待选择的列表
                         marketNameSerarch = criteriaBuilder.equal(market.get("name"),marketName);
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"待选择");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        criteriaQuery.where(p,marketNameSerarch,predicate);

                    }else {
                        //管理员所属的市场为总部,这是管理员可以按市场进行门店的搜索
                        //（门店状态是待审批）
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"待审批");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        p=criteriaBuilder.or(p1, p2, p3);
                        criteriaQuery.where(p,predicate);
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
    public List<Store> findByMarketName(String marketName){
        return storeRepository.findByMarketName(marketName);
    }
    public Page<Store> adjustList(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<MarketEntity> market = root.get("marketCode");  //根据门店所属市场进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market.get("name"),"%"+values+"%");
                    Predicate p = criteriaBuilder.or(p1, p2);
                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketEntity().getName();
                    Predicate marketNameSerarch=null;
                    Predicate storeStatusNameWait = null;
                    // Predicate stpreStatusNameChange = null;
                    //按门店状态进行分配
                    Path<StoreStatus> storeStatus = root.get("storeStatus");
                    if(!marketName.equals("总部")){
                        //非管理员操作（门店状态是待选择/待选择）
                        //开始的展示列表事待选择的列表
                        marketNameSerarch = criteriaBuilder.equal(market.get("name"),marketName);
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"待调整");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        criteriaQuery.where(p,marketNameSerarch,predicate);

                    }else {
                        //管理员所属的市场为总部,这是管理员可以按市场进行门店的搜索
                        //（门店状态是待审批）
                        storeStatusNameWait = criteriaBuilder.equal(storeStatus.get("statusName"),"已确认");
                        Predicate predicate = criteriaBuilder.or(storeStatusNameWait);
                        p=criteriaBuilder.or(p1, p2, p3);
                        criteriaQuery.where(p,predicate);
                    }
                    return null;
                }
            },pageable);
        }
        return storeRepository.findAll(pageable);
    }
    /*public List<Store> findReport(){
        List<Store> storeList  = new ArrayList<>();

        List<Store> all = storeRepository.findAll();
        System.out.println("allSize---->"+all.size());
        for (int i = 0; i < all.size(); i++) {
            Store store = all.get(i);
            if(store.getStoreStatus().getStatusName().equals("已确认")){
                storeList.add(store);
            }
        }
        System.out.println("report---->"+storeList.size());
        return  storeList;
    }*/
    public List<Store> findByStoreStatus(){
        return storeRepository.findByStoreStatus("已确认");
    }


}
