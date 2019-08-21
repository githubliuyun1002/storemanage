package com.xiabuxiabu.storemanage.service.ccuser;

import com.xiabuxiabu.storemanage.entity.ccuser.CCUserType;
import com.xiabuxiabu.storemanage.repository.ccuser.CCUserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CCUserTypeServive {
    @Autowired
    private CCUserTypeRepository ccUserTypeRepository;
    public List<CCUserType> findAll(){
        return  ccUserTypeRepository.findAll();
    }
}
