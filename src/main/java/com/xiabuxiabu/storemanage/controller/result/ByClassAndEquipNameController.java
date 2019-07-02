package com.xiabuxiabu.storemanage.controller.result;

import com.xiabuxiabu.storemanage.entity.publicutil.ResultByClassAndEquipName;
import com.xiabuxiabu.storemanage.service.equip.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/result")
public class ByClassAndEquipNameController {
    @Autowired
    private ClassificationService classificationService;
    /**
     * 按条件查询事件
     * @param page
     * @param pageSize
     * @param className
     * @param equipName
     * @return
     */
    @RequestMapping("/findContionAll")
    public List<ResultByClassAndEquipName> findAll(@RequestParam("page")int page,
                                                   @RequestParam("pageSize") int pageSize,
                                                   @RequestParam("className") String className,
                                                   @RequestParam("equipName") String equipName){
        List<ResultByClassAndEquipName> list =
                classificationService.findSearch(page,pageSize,className,equipName);
        String keys="";
        String value="";
        for (int i = 0; i < list.size(); i++) {
            try{
                //转换为object
                Object object = (Object)list.get(i);
                //将object 转换为 object[] 即可获取对应的值了。

                Object[] objarray = (Object[])object;

                //获取对应的值

                keys =String.valueOf(objarray[0]);

                value =String.valueOf(objarray[1]);

                System.out.println(keys);

                System.out.println(value);



            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("obj--->"+list.get(i).toString());
        }
        return list;

    }
}
