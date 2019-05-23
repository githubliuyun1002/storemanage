package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.repository.equip.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    public List<Item> findAll(){
        return  itemRepository.findAll();
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }
}
