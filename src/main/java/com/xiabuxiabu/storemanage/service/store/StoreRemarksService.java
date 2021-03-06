package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.StoreRemarks;
import com.xiabuxiabu.storemanage.repository.store.StoreRemarksRepository;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class StoreRemarksService {
    @Autowired
    private StoreRemarksRepository storeRemarksRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;
    public StoreRemarks save(StoreRemarks storeRemarks){
        return storeRemarksRepository.save(storeRemarks);
    }

    public int deleteByItemsId(int itemsId) {
        return storeRemarksRepository.deleteByItemsId(itemsId);
    }

    public StoreRemarks findByItemsId(int itemsId){
        return storeRemarksRepository.findByItemsId(itemsId);
    }

    public void  deleteById(int id){
        storeRemarksRepository.deleteById(id);
    }
    public StoreRemarks findById(int id){
        return storeRemarksRepository.findById(id).get();
    }
    public List<StoreRemarks> findAll(){
        return  storeRemarksRepository.findAll();
    }
    //日志实现分页and搜索
    public Page<StoreRemarks> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.DESC,"checkTime");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRemarksRepository.findAll(new Specification<StoreRemarks>() {
                @Override
                public Predicate toPredicate(Root<StoreRemarks> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("storeName");  //餐厅名称进行查询
                    Path<String> code = root.get("storeCode");  //餐厅编码进行查询
                    Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                    Path<String> itemName = root.get("itemName");  //根据设备名称进行搜索
                    Path<String>  keyword = root.get("storeAnditem");  //搜索的关键词进行在搜索
                    Path<String> checkMan = root.get("checkMan");
                    Path<Date> checkTime =  root.get("checkTime");

                  //  Path<String> band= root.get("band");  //根据门店的品牌进行查询

                    Predicate p1 = criteriaBuilder.like(name,"%"+ values +"%");
                    Predicate p2 = criteriaBuilder.like(code,"%"+ values +"%");
                    Predicate p3 = criteriaBuilder.like(market,"%"+values+"%");
                  //  Predicate p4 = criteriaBuilder.like(band,"%"+values+"%");
                    Predicate p5 = criteriaBuilder.like(itemName,"%"+values+"%");
                    Predicate p6 = criteriaBuilder.like(keyword,"%"+values+"%");


                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();
                    String bandName = userService.findByUserName(userName).getBand();

                    Predicate chenckPerson = criteriaBuilder.isNotNull(checkMan);
                    Predicate check = criteriaBuilder.isNotNull(checkTime);


                    Predicate marketNameSerarch=null;
                    Predicate bandSearch = null;
                    if(!marketName.equals("总部")){
                        marketNameSerarch = criteriaBuilder.equal(market,marketName);
                     //   bandSearch = criteriaBuilder.equal(band,bandName);
                        Predicate predicate = criteriaBuilder.or(p1,p2,p5,p6);
                        criteriaQuery.where(predicate,marketNameSerarch,chenckPerson,check);
                    }else{
                        Predicate or = criteriaBuilder.or(p1,p2,p3,p5,p6);
                        criteriaQuery.where(or,chenckPerson,check);
                    }
                    return null;
                }
            },pageable);
        }
        return storeRemarksRepository.findAll(pageable);

    }

    /**
     * 按条件查询，返回所有的记录
     * @return
     */
    public List<StoreRemarks> findAllList(){
        Sort sort = new Sort(Sort.Direction.DESC,"checkTime");
        return storeRemarksRepository.findAll(new Specification<StoreRemarks>() {
            @Override
            public Predicate toPredicate(Root<StoreRemarks> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<String> checkMan = root.get("checkMan");
                Path<Date> checkTime =  root.get("checkTime");
                Path<String> market = root.get("marketName");  //根据门店所属市场进行查询
                //检查人不为空，时间不为空
                Predicate chenckPerson = criteriaBuilder.isNotNull(checkMan);
                Predicate check = criteriaBuilder.isNotNull(checkTime);

                String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                String marketName = userService.findByUserName(userName).getMarketName();
                //市场IT
                if(!marketName.equals("总部")){
                    Predicate marketNameSerarch = criteriaBuilder.equal(market,marketName);
                    criteriaQuery.where(marketNameSerarch,chenckPerson,check);
                }else{

                    //管理员
                    criteriaQuery.where(chenckPerson,check);
                }

                return null;
            }
        },sort);

    }



}
