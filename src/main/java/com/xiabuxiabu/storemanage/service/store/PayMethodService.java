package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.PayMethod;
import com.xiabuxiabu.storemanage.repository.store.PayMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayMethodService {
    @Autowired
    private PayMethodRepository payMethodRepository;
    public List<PayMethod> findAll(){
        return payMethodRepository.findAll();
    }
}
