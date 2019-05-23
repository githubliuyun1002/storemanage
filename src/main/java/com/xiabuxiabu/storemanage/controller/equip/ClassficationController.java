package com.xiabuxiabu.storemanage.controller.equip;

import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.result.ResultByClassAndEquipName;
import com.xiabuxiabu.storemanage.service.equip.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/classfication")
public class ClassficationController {
     @Autowired
     private ClassificationService classificationService;
     @RequestMapping("/home")
     public String home(){
         return "/equip/classfication";
     }

     @RequestMapping("/findAllList")
     @ResponseBody
     public Page<Classification> findAll(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
            return classificationService.findAllList(page,pageSize,searchName);
        }
     @RequestMapping("/findAll")
     @ResponseBody
     public List<Classification> findAll(){
          return classificationService.findAll();
     }
     @RequestMapping("/save")
     public String save(Classification classification){
          if(classification.getClassId()!=0){
               Classification demo = classificationService.findById(classification.getClassId());
               classification.setEquipNames(demo.getEquipNames());
          }
          classificationService.save(classification);
          return "redirect:/classfication/home";
     }
     @RequestMapping("/findById")
     @ResponseBody
     public Classification findById(int id){
          return classificationService.findById(id);
     }








}
