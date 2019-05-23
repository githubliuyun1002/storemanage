package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.repository.store.StoreStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreStatusService {
    @Autowired
    private StoreStatusRepository storeStatusRepository;
    public StoreStatus findById(int id){
        return storeStatusRepository.findById(id).get();
    }
    public List<StoreStatus> findAll(){
        return storeStatusRepository.findAll();
    }
    public StoreStatus save(StoreStatus storeStatus){
        return storeStatusRepository.save(storeStatus);
    }

}
