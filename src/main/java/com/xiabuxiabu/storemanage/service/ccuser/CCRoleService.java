package com.xiabuxiabu.storemanage.service.ccuser;

import com.xiabuxiabu.storemanage.entity.ccuser.CCRole;
import com.xiabuxiabu.storemanage.repository.ccuser.CCRoleRepository;
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
public class CCRoleService {
    @Autowired
    private CCRoleRepository ccRoleRepository;
    public List<CCRole> find(){
        return  ccRoleRepository.findAll();
    }
    public CCRole save(CCRole ccRole){
        return ccRoleRepository.save(ccRole);
    }
    public Page<CCRole> findAll(int page,int size,String values){
        //按照id排序
        Sort sort = new Sort(Sort.Direction.ASC,"roleId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            //按条件进行查询
            return ccRoleRepository.findAll(new Specification<CCRole>() {
                @Override
                public Predicate toPredicate(Root<CCRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                   //根据角色中文名字进行搜索
                    Path<String> name = root.get("name");
                    Predicate p1 = criteriaBuilder.like(name,"%"+values+"%");
                    criteriaQuery.where(p1);
                    return null;
                }
            },pageable);


        }
        return ccRoleRepository.findAll(pageable);



    }

    public CCRole findById(int roleId){
        return  ccRoleRepository.findById(roleId).get();
    }

}
