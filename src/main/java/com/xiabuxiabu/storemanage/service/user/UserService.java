package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.entity.user.Role;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.repository.user.RoleRepository;
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
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    public List<User> findAll(){
        return  userRepository.findAll();
    }
    public User findByUserName(String username){
        return userRepository.findByName(username);
    }
    public Page<User> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return  userRepository.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("name");  //人员的中文名
                    criteriaQuery.where(criteriaBuilder.like(name, "%" + values + "%"));
                    return null;
                }
            },pageable);
        }
        return  userRepository.findAll(pageable);
    }
    public List<Role> RoleList(){
        return roleRepository.findAll();
    }
    public User addSave(User user){
        return userRepository.save(user);
    }
    public User findById(int id){
        return userRepository.findById(id).get();
    }
    /**
     * 保存修改的数据
     */
    public User updateSave(User user){
        User u =userRepository.findById(user.getId()).get();
        if(u!=null){
            u.setUsername(user.getUsername());
            u.setName(user.getName());
            u.setPassword(user.getPassword());
            u.setMail(user.getMail());
            u.setPublicStatus(user.getPublicStatus());
            u.setRoles(user.getRoles());
        }
        return userRepository.save(user);
    }

}
