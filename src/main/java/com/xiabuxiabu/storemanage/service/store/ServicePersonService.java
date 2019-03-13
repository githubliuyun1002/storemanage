package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.ServicePerson;
import com.xiabuxiabu.storemanage.repository.store.ServicePersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePersonService {
    @Autowired
    private ServicePersonRepository servicePersonRepository;
    public List<ServicePerson> findAll(){
        return servicePersonRepository.findAll();
    }
}
