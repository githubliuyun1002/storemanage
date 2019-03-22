package com.xiabuxiabu.storemanage.controller.user;

import com.xiabuxiabu.storemanage.entity.user.Permission;
import com.xiabuxiabu.storemanage.entity.user.Role;
import com.xiabuxiabu.storemanage.service.user.PermissionService;
import com.xiabuxiabu.storemanage.service.user.RoleService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * 角色信息的表现层
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    /**
     * 角色列表的展示页面
     */
    @RequestMapping("/home")
    public String home(){
        return "/user/role";
    }

    /**
     * 查询role实体
     * @param page
     * @param pageSize
     * @param searchName
     * @return
     */
    @RequestMapping("/roleList")
    @ResponseBody
    public Page<Role> roleList(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return roleService.findAll(page,pageSize,searchName);
    }
    @RequestMapping("/findPermis")
    @ResponseBody
    public List<Permission> findPermis(){
        return permissionService.findAll();
    }
    @RequestMapping("/save")
    public String save(Role role){
        roleService.save(role);
        return "redirect:/role/home";
    }
    @RequestMapping("/findById")
    @ResponseBody
    public Role findById(int id){
        return roleService.findById(id);
    }
}
