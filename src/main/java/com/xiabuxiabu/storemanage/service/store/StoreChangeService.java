package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.StoreChange;
import com.xiabuxiabu.storemanage.repository.store.StoreChangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreChangeService {
    @Autowired
    private StoreChangeRepository storeChangeRepository;
    public StoreChange save(StoreChange storeChange){
        return storeChangeRepository.save(storeChange);
    }

}
