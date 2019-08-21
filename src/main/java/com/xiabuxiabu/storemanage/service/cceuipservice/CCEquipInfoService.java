package com.xiabuxiabu.storemanage.service.cceuipservice;

import com.xiabuxiabu.storemanage.entity.ccequip.CCEquipInfo;
import com.xiabuxiabu.storemanage.repository.ccequip.CCEquipInfoRepository;
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

@Service
public class CCEquipInfoService {
    @Autowired
    private CCEquipInfoRepository ccEquipInfoRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;

    public CCEquipInfo save(CCEquipInfo ccEquipInfo){
        return ccEquipInfoRepository.save(ccEquipInfo);
    }
    public Page<CCEquipInfo> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC, "weeks");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if(values!=null){
            return ccEquipInfoRepository.findAll(new Specification<CCEquipInfo>() {
                @Override
                public Predicate toPredicate(Root<CCEquipInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

                    Path<String> storeCode =  root.get("storeCode");
                    Path<String> storeName =  root.get("storeName");
                    Path<String> equipName =  root.get("equipName");
                    Path<String> market = root.get("marketName");
                    Predicate p1 = criteriaBuilder.like(storeCode,"%"+values+"%");
                    Predicate p2 = criteriaBuilder.like(storeName,"%"+values+"%");
                    Predicate p3 = criteriaBuilder.like(equipName,"%"+values+"%");
                    Predicate p4 = criteriaBuilder.like(market,"%"+values+"%");

                    Predicate or = criteriaBuilder.or(p1,p2,p3);
                    //criteriaQuery.where(or);
                    //拿到登录人的信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();

                    if(!marketName.equals("总部")){
                        Predicate marketPre = criteriaBuilder.equal(market,marketName);
                        criteriaQuery.where(or,marketPre);
                        criteriaQuery.distinct(true);

                    }else{
                        //对管理员进行查询
                        Predicate orr = criteriaBuilder.or(p1,p2,p3,p4);
                        criteriaQuery.where(orr);
                        criteriaQuery.distinct(true);

                    }





                    return null;
                }
            },pageable);
        }

        return  ccEquipInfoRepository.findAll(pageable);
    }
}
