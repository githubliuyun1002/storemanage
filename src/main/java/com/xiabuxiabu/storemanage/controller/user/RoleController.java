package com.xiabuxiabu.storemanage.controller.user;

import com.xiabuxiabu.storemanage.service.user.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 角色信息的表现层
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    /**
     * 角色列表的展示页面
     */
    @RequestMapping("/home")
    public String home(){
        return "/user/role";
    }
}
