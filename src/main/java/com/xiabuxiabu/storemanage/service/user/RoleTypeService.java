package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.entity.user.RoleType;
import com.xiabuxiabu.storemanage.repository.user.RoleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleTypeService {
    @Autowired
    private RoleTypeRepository roleTypeRepository;
    public List<RoleType> findAll(){
        return roleTypeRepository.findAll();
    }
}
