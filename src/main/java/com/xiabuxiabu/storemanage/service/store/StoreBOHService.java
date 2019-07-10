package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.StoreBOH;
import com.xiabuxiabu.storemanage.repository.store.StoreBOHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreBOHService {
    @Autowired
    private StoreBOHRepository storeBOHRepository;
    public List<StoreBOH> findALlStoreBOH(){
        return storeBOHRepository.findAll();
    }

    public List<String> marketNameList(){
        return storeBOHRepository.marketNameList();
    }
    public List<String> bandList(){
        return storeBOHRepository.bandList();
    }

    public List<String> bandMarketList(String band){
        return storeBOHRepository.bandMarketList(band);
    }

}
