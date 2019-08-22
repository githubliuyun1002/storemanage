package com.xiabuxiabu.storemanage.service.cceuipservice;

import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import com.xiabuxiabu.storemanage.repository.ccequip.CCItemsRepository;
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
public class CCItemsService {
    @Autowired
    private CCItemsRepository ccItemsRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;

    public ccItems  save(ccItems items){
        return ccItemsRepository.save(items);
    }

    public ccItems findById(int id){
        return ccItemsRepository.findById(id).get();
    }
    public void deleteById(int id){
        ccItemsRepository.deleteById(id);
    }

    public Page<ccItems> findAll(int page, int size, String values) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("itemsId"));//默认asc
        if(values!=null && !values.equals("")){
            return ccItemsRepository.findAll(new Specification<ccItems>() {
                @Override
                public Predicate toPredicate(Root<ccItems> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Join join = root.join("ccStoresSet",JoinType.LEFT);
                    Path<String> name = join.get("storeName");
                    Path<String> code = join.get("storeCode");
                    Path<String> market = join.get("marketName");

                    Predicate p1 = criteriaBuilder.like(name, "%" + values + "%");
                    Predicate p2 = criteriaBuilder.like(code, "%" + values + "%");
                    Predicate p3 = criteriaBuilder.like(market, "%" + values + "%");
                    Predicate p4 = criteriaBuilder.like(root.get("keywords"), "%" + values + "%");
                    Predicate isNotNull = criteriaBuilder.isNotNull(root.get("lastDate"));
                    Predicate or = criteriaBuilder.or(p1, p2, p4);
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    String marketName = userService.findByUserName(userName).getMarketName();
                    if(!marketName.equals("总部")){
                        Predicate marketPre = criteriaBuilder.equal(market, marketName);
                        criteriaQuery.where(or, marketPre, isNotNull);
                        criteriaQuery.distinct(true);

                    }else{
                        Predicate orr = criteriaBuilder.or(p1, p2, p3, p4);
                        criteriaQuery.where(orr, isNotNull);
                        criteriaQuery.distinct(true);
                    }
                    return null;
                }
            },pageable);
        }
        return ccItemsRepository.findAll(pageable);
    }





}
