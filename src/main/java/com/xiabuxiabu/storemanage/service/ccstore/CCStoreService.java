package com.xiabuxiabu.storemanage.service.ccstore;

import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;
import com.xiabuxiabu.storemanage.repository.ccstore.CCStoreRepository;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CCStoreService {
    @Autowired
    private CCStoreRepository ccStoreRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;

    public Page<CCStore> findAllList(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"storeId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return ccStoreRepository.findAll(new Specification<CCStore>() {
                @Override
                public Predicate toPredicate(Root<CCStore> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Predicate p1 = criteriaBuilder.like(name,"%"+values+"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+values+"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();

                    if(!marketName.equals("总部")){
                        Predicate marketPre = criteriaBuilder.equal(market,marketName);
                        Predicate orr = criteriaBuilder.or(p1,p2);
                        criteriaQuery.where(orr,marketPre);
                        criteriaQuery.distinct(true);

                    }else{
                        //对管理员进行查询
                        Predicate orr = criteriaBuilder.or(p1,p2,p3);
                        criteriaQuery.where(orr);
                        criteriaQuery.distinct(true);

                    }
                    return null;
                }
            },pageable);
        }
        return ccStoreRepository.findAll(pageable);
    }

    /**
     * 整合门店得设备信息
     * @param page
     * @param size
     * @param values
     * @return
     */
    public Page<CCStore> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"ccItemsSet.itemsId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return ccStoreRepository.findAll(new Specification<CCStore>() {
                @Override
                public Predicate toPredicate(Root<CCStore> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
//                    Path<String> keywords =  root.get("keywords");

                    //根据门店设备进行查询

                    Predicate p1 = criteriaBuilder.like(name,"%"+values+"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+values+"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");

                    Join join = root.join(root.getModel().getSet("ccItemsSet",ccItems.class),JoinType.LEFT);

                    //对门店得设备名称进行查询
                    Predicate p4 = criteriaBuilder.like(join.get("keywords"),"%"+values+"%");

                    //维护周期不为空得条件
                    Predicate isNotNull =  criteriaBuilder.isNotNull(join.get("lastDate"));

                    Predicate or = criteriaBuilder.or(p1,p2,p4);

                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();

                    if(!marketName.equals("总部")){
                        Predicate marketPre = criteriaBuilder.equal(market,marketName);
                        criteriaQuery.where(or,marketPre,isNotNull);
                        criteriaQuery.distinct(true);

                    }else{
                        //对管理员进行查询
                        Predicate orr = criteriaBuilder.or(p1,p2,p3,p4);
                        criteriaQuery.where(orr,isNotNull);
                        criteriaQuery.distinct(true);

                    }
                    return null;
                }
            },pageable);
        }
        return ccStoreRepository.findAll(pageable);

    }

    public CCStore findById(int id){
        return ccStoreRepository.findById(id).get();
    }
    public CCStore findByStoreCode(String storeCode){
        return ccStoreRepository.findByStoreCode(storeCode);
    }
    public CCStore save(CCStore ccStore){
        return  ccStoreRepository.save(ccStore);
    }

    public List<CCStore> findAllList(){
        return ccStoreRepository.findAll();
    }
    public List<CCStore> findByMarketName(String marketName){
        return  ccStoreRepository.findByMarketName(marketName);
    }



}
