package com.xiabuxiabu.storemanage.service.cceuipservice;

import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import com.xiabuxiabu.storemanage.repository.ccequip.CCItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CCItemsService {
    @Autowired
    private CCItemsRepository ccItemsRepository;
    public ccItems  save(ccItems items){
        return ccItemsRepository.save(items);
    }

    public ccItems findById(int id){
        return ccItemsRepository.findById(id).get();
    }
    public void deleteById(int id){
        ccItemsRepository.deleteById(id);
    }



}
