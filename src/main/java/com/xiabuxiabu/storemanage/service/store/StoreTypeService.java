package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.StoreType;
import com.xiabuxiabu.storemanage.repository.store.StoreTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreTypeService {
    @Autowired
    private StoreTypeRepository storeTypeRepository;
    public StoreType save(StoreType storeType){
        return storeTypeRepository.save(storeType);
    }
    public List<StoreType> findTypeByStoreId(int storeId){
        return storeTypeRepository.findTypeByStoreId(storeId);
    }
    public StoreType findStore(int storeId,int typeId){
        return storeTypeRepository.findStore(storeId,typeId);
    }
    public StoreType findById(int id){
        return  storeTypeRepository.findById(id).get();
    }
}
