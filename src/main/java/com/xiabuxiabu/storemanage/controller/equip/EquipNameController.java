package com.xiabuxiabu.storemanage.controller.equip;

import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.service.equip.ClassificationService;
import com.xiabuxiabu.storemanage.service.equip.EquipNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/equipname")
public class EquipNameController {
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private EquipNameService equipNameService;
    @RequestMapping("/home")
    public String home(){
        return "/equip/equipname";
    }
    @RequestMapping("/save")
    public String save(int classId,String[] equipName){   //classId
        Set<EquipName> equipNameSet =new  HashSet<>();
        for (int i = 0; i <equipName.length ; i++) {
            EquipName equipNameEntity = new EquipName();
            equipNameEntity.setName(equipName[i]);
            equipNameService.save(equipNameEntity);
            equipNameSet.add(equipNameEntity);
        }
        Classification classification = classificationService.findById(classId);
        //如果原来的设备类别中有设备名称的实体
        if(classification.getEquipNames()!=null){
            Set<EquipName> set = classification.getEquipNames();
            equipNameSet.addAll(set);
            classification.setEquipNames(equipNameSet);
        }else{
            classification.setEquipNames(equipNameSet);
        }
        classificationService.save(classification);
        return  "redirect:/equipname/home";
    }

    @RequestMapping("/findAllList")
    @ResponseBody
    public Page<EquipName> findAllList(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return equipNameService.findAllList(page,pageSize,searchName);
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public List<EquipName> findAll(){
        return equipNameService.findAll();
    }

    //修改设备名称时，有添加新的设备名称
    @RequestMapping("/updateSave")
    public String updateSave(Classification classification,String[] upEquipName){
        Set<EquipName> equipNames = classification.getEquipNames();

        for (int i = 0; i <upEquipName.length ; i++) {
           EquipName equipName = new EquipName();
           equipName.setName(upEquipName[i]);
           equipNameService.save(equipName);
           equipNames.add(equipName);
        }
        classification.setEquipNames(equipNames);
        classificationService.save(classification);
        return "redirect:/equipname/home";
    }
    //修改时，不添加新的设备名称
    @RequestMapping("/update")
    public String updateSave(Classification classification){
        classificationService.save(classification);
        return "redirect:/equipname/home";
    }



    @RequestMapping("/findById")
    @ResponseBody
    public EquipName findById(int id){
        return equipNameService.findById(id);
    }




}
