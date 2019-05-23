package com.xiabuxiabu.storemanage.controller.user;

import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;
    /**
     * 登陆页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){

        return "/login";
    }
    /**
     * 登录成功的home首页
     */
    @RequestMapping("/home")
    public String home(){
        return "/home";
    }

    @RequestMapping("/info")
    public String info(){
        return "/common/info";
    }

}
