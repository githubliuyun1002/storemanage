package com.xiabuxiabu.storemanage.controller.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.service.equip.EquipNameService;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import com.xiabuxiabu.storemanage.service.publicutil.PublicStatusService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private EquipNameService equipNameService;
    @Autowired
    private PublicStatusService publicStatusService;
    //设备类型展示页面
    @RequestMapping("/home")
    public String home(){
        return "/equip/item";
    }

    @RequestMapping("/save")
    public String save(int equipId,String[] name){
        EquipName equipName = equipNameService.findById(equipId);
       // System.out.println("equipId---->"+equipId);
        Set<Item> itemSet = new HashSet<>();
        for (int i = 0; i <name.length ; i++) {
            Item item = new Item();
            item.setName(name[i]);
            item.setClassId(equipName.getClassId());
            item.setClassName(equipName.getClassName());
            item.setEquipId(equipId);
            item.setEquipName(equipName.getName());
            item.setCode(equipName.getName()+""+name[i]);
            PublicStatus publicStatus = publicStatusService.findById(1);
            item.setPublicStatus(publicStatus);
            itemService.save(item);
            itemSet.add(item);
        }

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
    public String upadteSave(EquipName equipNameEntity,String[] upName){
        System.out.println("进入的url是-------》updateSave");
        Set<Item> itemSet = equipNameEntity.getItemSet();
        if(upName.length!=0){
            for (int i = 0; i <upName.length ; i++) {
                Item item = new Item();
                item.setName(upName[i]);
                itemService.save(item);
                itemSet.add(item);
            }
        }
        equipNameEntity.setItemSet(itemSet);
        System.out.println("equipName--->"+equipNameEntity);
        equipNameService.save(equipNameEntity);
        return "redirect:/item/home";
    }
    //执行修改操作没有添加新的设备类型
    @RequestMapping("/update")
    public String upadteSave(Item item){
        System.out.println("items------>"+item);
        if(itemService.findById(item.getItemId())!=null){
            Item itemDB = itemService.findById(item.getItemId());
            itemDB.setPublicStatus(item.getPublicStatus());
            itemDB.setName(item.getName());
            itemDB.setClassName(item.getClassName());
            itemService.save(itemDB);
        }

        return "redirect:/item/home";
    }
    @RequestMapping("/findAllItem")
    @ResponseBody
    public Page<Item> findAllItem(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
         return itemService.findAllItem(page,pageSize,searchName);
    }
    @RequestMapping("/findById")
    @ResponseBody
    public Item findById(int itemId){
        return itemService.findById(itemId);
    }
    @RequestMapping("/findByName")
    @ResponseBody
    public Map<String,Object> findByName(String itemName,String equipName){
        Map<String,Object> map = new HashMap<>();
        if(itemService.findByName(itemName,equipName)!=null){
            map.put("code","true");
        }else{
            map.put("code","false");
        }
        return  map;
    }





}
