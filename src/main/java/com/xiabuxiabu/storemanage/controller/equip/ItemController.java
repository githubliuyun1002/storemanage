package com.xiabuxiabu.storemanage.controller.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.service.equip.EquipNameService;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private EquipNameService equipNameService;
    //设备类型展示页面
    @RequestMapping("/home")
    public String home(){
        return "/equip/item";
    }
    @RequestMapping("/save")
    public String save(int equipId,String[] name){
        System.out.println("equipId---->"+equipId);
        Set<Item> itemSet = new HashSet<>();
        for (int i = 0; i <name.length ; i++) {
            Item item = new Item();
            item.setName(name[i]);
            itemService.save(item);
            itemSet.add(item);
        }
        EquipName equipName = equipNameService.findById(equipId);
        //如果原来的设备名称中又设备类型
        if(equipName.getItemSet()!=null){
           Set<Item> items =  equipName.getItemSet();
           itemSet.addAll(items);
           equipName.setItemSet(itemSet);
        }else {
            equipName.setItemSet(itemSet);
        }
        equipNameService.save(equipName);
        return "redirect:/item/home";      //跳转到页面展示页
    }
    //执行修改的操作
    @RequestMapping("/updateSave")
    public String upadteSave(EquipName equipNameEntity){
        System.out.println("equipName--->"+equipNameEntity);
        equipNameService.save(equipNameEntity);
        return "redirect:/item/home";
    }



}
