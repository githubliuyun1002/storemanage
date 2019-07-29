package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.repository.store.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsService {
    @Autowired
    private ItemsRepository itemsRepository;
    public Items save(Items items){
        return  itemsRepository.save(items);
    }
    public Items findById(int id){
        return itemsRepository.findById(id).get();
    }
    public List<Items> findAll(){
        return  itemsRepository.findAll();
    }

    public void deleteById(int itemsId){
        itemsRepository.deleteById(itemsId);
    }




}
