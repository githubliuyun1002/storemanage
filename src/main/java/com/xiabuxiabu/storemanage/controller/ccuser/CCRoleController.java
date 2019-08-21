package com.xiabuxiabu.storemanage.controller.ccuser;

import com.xiabuxiabu.storemanage.entity.ccuser.CCPermission;
import com.xiabuxiabu.storemanage.entity.ccuser.CCRole;
import com.xiabuxiabu.storemanage.service.ccuser.CCPersmissionService;
import com.xiabuxiabu.storemanage.service.ccuser.CCRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/ccrole")
public class CCRoleController {
    @Autowired
    private CCRoleService ccRoleService;
    @Autowired
    private CCPersmissionService ccPersmissionService;

    @RequestMapping("/home")
    public String home(){
        return "/ccuser/role";
    }
    @RequestMapping("/save")
    public String save(CCRole ccRole){
        //System.out.println("ccRole----->"+ccRole);
        ccRoleService.save(ccRole);
        return "redirect:/ccrole/home";
    }
    @RequestMapping("/findAll")
    @ResponseBody
    public Page<CCRole> findAll(int page,int pageSize,String searchName){
        return ccRoleService.findAll(page,pageSize,searchName);
    }
    @RequestMapping("/findAllPermis")
    @ResponseBody
    public List<CCPermission> findAllPermis(){
        return ccPersmissionService.findAll();
    }
    @RequestMapping("/findById")
    @ResponseBody
    public CCRole findById(int roleId){
        return ccRoleService.findById(roleId);
    }





}
