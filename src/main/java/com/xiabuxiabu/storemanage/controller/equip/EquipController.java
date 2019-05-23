package com.xiabuxiabu.storemanage.controller.equip;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/equip")
public class EquipController {
    /**
     * 设备展示首页
     * @return
     */
    @RequestMapping("/home")
    public String home(){
        return "/equip/home";
    }
    @RequestMapping("/left")
    public String left(){
        //return "/equip/home";
        return "/equip/left";
    }
    @RequestMapping("/right")
    public String right(){
        //return "/equip/home";
        return "/equip/list";
    }



    /**
    * 查询List数据*/


    /*@RequestMapping("/findAll")
    @ResponseBody
    public Page<EquipEntity> findAll(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize,@RequestParam("searchName") String searchName){
        return equipService.findAll(page,pageSize,searchName);
    }*/

/**
     * 保存新增设备数据

    @RequestMapping("/addSave")
    public String addSave(EquipEntity equipEntity,String[] typeName){
        //保存设备类型
        for(int i=0;i<typeName.length;i++){
            TypeEntity typeEntity = new TypeEntity();
            typeEntity.setTypeName(typeName[i]);
            typeService.save(typeEntity);
        }
        //通过设备类型得到id
        Set<TypeEntity>  typeEntities = new HashSet<>();
        for (int j = 0; j <typeName.length ; j++) {
            TypeEntity ty= new TypeEntity();
             ty= typeService.findByName(typeName[j]);
             typeEntities.add(ty);
            equipEntity.setEquipTypes(typeEntities);
        }
        equipService.addSave(equipEntity);
        return "redirect:/equip/home";
    }*/
   /* @RequestMapping("/findById")
    @ResponseBody
    public EquipEntity findById(int id){
        return equipService.findById(id);
    }
    @RequestMapping("/updateSave")
    public String updateSave(EquipEntity equipEntity,String[] update_typeName){
        //保存类型
        for (int i = 0; i <update_typeName.length ; i++) {
            TypeEntity typeEntity = new TypeEntity();
            typeEntity.setTypeName(update_typeName[i]);
            typeService.save(typeEntity);
        }
        Set<TypeEntity>  typeEntities = new HashSet<>();
        for (int m = 0; m <update_typeName.length ; m++) {
            TypeEntity updatety= new TypeEntity();
            updatety= typeService.findByName(update_typeName[m]);
            typeEntities.add(updatety);
        }
        Set<TypeEntity> equipTypes = equipEntity.getEquipTypes();
        equipTypes.addAll(typeEntities);
        equipEntity.setEquipTypes(equipTypes);
        equipService.addSave(equipEntity);
        return "redirect:/equip/home";
    }
    @RequestMapping("/update")
    public String update(EquipEntity equipEntity){
        equipService.addSave(equipEntity);
        return "redirect:/equip/home";
    }
    @RequestMapping("/allList")
    @ResponseBody
    public List<EquipEntity> allList(){
        return equipService.findAll();
    }
*/


}
