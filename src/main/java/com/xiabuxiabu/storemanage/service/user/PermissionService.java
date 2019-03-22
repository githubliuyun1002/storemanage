package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.entity.user.Permission;
import com.xiabuxiabu.storemanage.repository.user.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    public List<Permission> findAll(){
        return  permissionRepository.findAll();
    }
}
