package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.entity.user.UserType;
import com.xiabuxiabu.storemanage.repository.user.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleTypeService {
    @Autowired
    private UserTypeRepository roleTypeRepository;
    public List<UserType> findAll(){
        return roleTypeRepository.findAll();
    }
}
