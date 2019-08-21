package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.entity.user.Role;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.entity.user.UserType;
import com.xiabuxiabu.storemanage.repository.user.RoleRepository;
import com.xiabuxiabu.storemanage.repository.user.UserTypeRepository;
import com.xiabuxiabu.storemanage.repository.user.UserRepository;
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
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserTypeRepository roleTypeRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;

    public List<User> findAll() {

        return userRepository.findAll();
    }

    public User findByUserName(String username) {
        return userRepository.findByName(username);
    }

    //人员这，管理员进行人员的操作。
    //此时管理员需要进行按，用户名、中文名、品牌、所属市场的操作
    public Page<User> findAll(int page, int size, String values) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if (values != null) {
            return userRepository.findAll(new Specification<User>() {
                @Override
                public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("name");  //人员的中文名
                    Path<String> band = root.get("band");  //根据品牌进行搜索
                    Path<String> marketName = root.get("marketName");  //根据人员的市场进行查询
                    Path<String> sign = root.get("sign");

                    Predicate p1 = criteriaBuilder.like(name, "%" + values + "%");
                    Predicate p2 = criteriaBuilder.like(marketName,"%"+values+"%");
                    Predicate p3 = criteriaBuilder.like(band,"%"+values+"%");
                    Predicate goals = criteriaBuilder.or(p1,p2,p3);
                    Predicate equal =  criteriaBuilder.equal(sign,"it");
                    criteriaQuery.where(goals,equal);
                    return null;
                }
            }, pageable);
        }
        return userRepository.findAll(pageable);
    }

    public List<UserType> findAllRole() {
        return roleTypeRepository.findAll();
    }

    public List<Role> RoleList() {
        return roleRepository.findAll();
    }

    public User addSave(User user) {
        return userRepository.save(user);
    }

    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    /**
     * 保存修改的数据
     */
    public User updateSave(User user) {
        User u = userRepository.findById(user.getId()).get();
        if (u != null) {
            u.setUsername(user.getUsername());
            u.setName(user.getName());
            u.setPassword(user.getPassword());
            u.setMail(user.getMail());
            u.setPublicStatus(user.getPublicStatus());
            u.setRoles(user.getRoles());
        }
        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<String> marketList() {
        return userRepository.marketList();

    }
    public List<User> findByMarketName(String name,String sign){
        return  userRepository.findByMarketName(name,sign);
    }
}

