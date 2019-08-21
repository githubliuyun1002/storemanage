package com.xiabuxiabu.storemanage.service.ccuser;

import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class CCUserService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if(values!=null){
            return userRepository.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("name");              //人员的中文名
                    Path<String> marketName = root.get("marketName");  //根据人员的市场进行查询
                    Path<String> sign = root.get("sign");              //根据标识人员标识，选出特殊设备得人员
                    Predicate p1 = criteriaBuilder.like(name, "%" + values + "%");
                    Predicate p2 = criteriaBuilder.like(marketName,"%"+values+"%");
                    Predicate or = criteriaBuilder.or(p1,p2);
                    Predicate equal =  criteriaBuilder.equal(sign,"teshu");
                    criteriaQuery.where(or,equal);
                    return null;
                }
            },pageable);

        }
        return userRepository.findAll(pageable);
    }
    public User save(User user){
        return userRepository.save(user);
    }
    public User findById(int id){
        return  userRepository.findById(id).get();
    }

    public List<User> ccfindByMarketName(String marketName,String sign){
        return  userRepository.ccfindByMarketName(marketName,sign);
    }


}
