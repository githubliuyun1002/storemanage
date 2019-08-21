package com.xiabuxiabu.storemanage.controller.cceuip;

import com.xiabuxiabu.storemanage.entity.ccequip.ccEquip;
import com.xiabuxiabu.storemanage.service.cceuipservice.CCEquipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ccequip")
public class CCEquipController {
    @Autowired
    private CCEquipService ccEquipService;

    @RequestMapping("/home")
    public String home(){
        return "/ccequip/ccequip";
    }

    @RequestMapping("/findAll")
    @ResponseBody
    public Page<ccEquip> findAll(int page,int pageSize,String searchName){
        return ccEquipService.findAll(page,pageSize,searchName);
    }
    @RequestMapping("/save")
    public String save(ccEquip ccEquip){
        ccEquipService.save(ccEquip);
        return "redirect:/ccequip/home";
    }

    @RequestMapping("/findByName")
    @ResponseBody
    public Map<String,Object> findByName(String name){
        Map<String,Object> map = new HashMap<>();
        if(ccEquipService.findByName(name)!=null){
            map.put("code","true");
        }else{
            map.put("code","false");
        }
        return map;
    }
    @RequestMapping("/findById")
    @ResponseBody
    public ccEquip findById(int id){
        return ccEquipService.findById(id);
    }

    /**
     * 修改设备的事件
     * @param ccEquip
     * @return
     */
    @RequestMapping("/updateSave")
    public String updateSave(ccEquip ccEquip){
        ccEquipService.save(ccEquip);
        return "redirect:/ccequip/home";
    }
}
