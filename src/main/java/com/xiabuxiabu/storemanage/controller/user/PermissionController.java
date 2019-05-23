package com.xiabuxiabu.storemanage.controller.user;

import com.xiabuxiabu.storemanage.entity.user.Permission;
import com.xiabuxiabu.storemanage.service.user.PermissionService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/permis")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @RequestMapping("/findAll")
    @ResponseBody
    public List<Permission> findAllByPid(int pid){
        return  permissionService.findAllByPid(pid);
    }
}
