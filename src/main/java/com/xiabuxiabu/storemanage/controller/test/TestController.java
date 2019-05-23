/*
package com.xiabuxiabu.storemanage.controller.test;

import com.alibaba.fastjson.JSON;
import com.xiabuxiabu.storemanage.entity.test.*;
import com.xiabuxiabu.storemanage.service.test.ClassificationService;
import com.xiabuxiabu.storemanage.service.test.EquipNameService;
import com.xiabuxiabu.storemanage.service.test.ItemService;
import com.xiabuxiabu.storemanage.service.test.StoreTestService;
import net.bytebuddy.asm.Advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private EquipNameService equipNameService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private StoreTestService storeTestService;
    @RequestMapping("/home")
    public String home(){
        return  "/test/test";
    }
    @RequestMapping("/show")
    public String hebing(){
        return "/test/show";
    }
    @RequestMapping("/findAllItem")
    @ResponseBody
    public List<Item> findAllItem(){
        return  itemService.findAll();
    }
    @RequestMapping("/findAllEquipType")
    @ResponseBody
    public List<EquipName> findAllEquipType(){
        return equipNameService.findAll();
    }
    @RequestMapping("/findAllClass")
    @ResponseBody
    public Map<String,Object> findAllClass(){
        Map<String,Object> map= new HashMap<>();
        map.put("classitem",classificationService.findAll());


        List<StoreTest> listStore = storeTestService.findAll();
        String store="[";
        for (int i = 0; i <storeTestService.findAll().size() ; i++) {
            int index = 1;
            store+="{\"id\":"+"\""+listStore.get(i).getId()+"\","+
                    "\"name\":"+"\""+listStore.get(i).getStoreName()+"\""+
                    ",\"storeCode\":" +"\""+listStore.get(i).getStoreCode()+"\"" + ",\"items\":{";
           // Set<Items> items = listStore.get(i).getItems();
            for(Object itemsset:listStore.get(i).getItems()){
                 Items items = (Items)itemsset;
                 store += "\""+items.getItem().getCode()+"\":"+ items.getNum();
                 if(index!=listStore.get(i).getItems().size()){
                     store+=",";
                }
                index++;
            }
            store+="}}";
            if(i!=listStore.size()-1){
                store+=",";
            }
        }
        store+="]";
        System.out.println("store--->"+store);
        map.put("store",store);
 map.put("store",storeTestService.findAll());

        return map;
    }
    @RequestMapping("/findByClassId")
    @ResponseBody
    public Classification findByClassId(int id){
        return classificationService.findById(id);
    }
    @RequestMapping("/fingByEquipNameId")
    @ResponseBody
    public EquipName fingByEquipNameId(int id){
        return equipNameService.findById(id);
    }
    @RequestMapping("/findStoreList")
    @ResponseBody
    public List<StoreTest> findStoreList(){
        System.out.println("storeDate--->"+storeTestService.findAll());
        return storeTestService.findAll();
    }

}
*/
