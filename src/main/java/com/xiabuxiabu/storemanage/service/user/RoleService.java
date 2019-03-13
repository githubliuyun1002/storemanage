package com.xiabuxiabu.storemanage.service.user;

import com.xiabuxiabu.storemanage.repository.user.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


}
