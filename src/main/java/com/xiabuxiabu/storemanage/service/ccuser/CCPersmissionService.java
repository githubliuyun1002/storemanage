package com.xiabuxiabu.storemanage.service.ccuser;

import com.xiabuxiabu.storemanage.entity.ccuser.CCPermission;
import com.xiabuxiabu.storemanage.repository.ccuser.CCPermissRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CCPersmissionService {
    @Autowired
    private CCPermissRepository ccPermissRepository;
    public List<CCPermission> findAll(){
        return ccPermissRepository.findAll();
    }
}
