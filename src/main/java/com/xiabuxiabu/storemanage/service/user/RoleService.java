package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.entity.user.Role;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.repository.user.RoleRepository;
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
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public Page<Role> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return  roleRepository.findAll(new Specification<Role>() {
                @Override
                public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("name");  //人员的中文名
                    criteriaQuery.where(criteriaBuilder.like(name, "%" + values + "%"));
                    return null;
                }
            },pageable);
        }
        return  roleRepository.findAll(pageable);
    }
    public Role save(Role role){
        return roleRepository.save(role);
    }
    public Role findById(int id){
        return  roleRepository.findById(id).get();
    }
}
