package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.AccessMethod;
import com.xiabuxiabu.storemanage.repository.store.AccessMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessMethodService {
    @Autowired
    private AccessMethodRepository accessMethodRepository;
    public List<AccessMethod> findAll(){

        return  accessMethodRepository.findAll();
    }
    public AccessMethod findById(int id){
        return accessMethodRepository.findById(id).get();
    }
}
