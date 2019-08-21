package com.xiabuxiabu.storemanage.controller.user;

import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @RequestMapping("/teshu")
    public String teshu(){
        return "/teshu";
    }
    @RequestMapping("/super")
    public String superHome(){
        return "/admin";
    }

    /**
     * 展示宽带到期情况列表
     * @return
     */
    @RequestMapping("/info")
    public String info(){
        return "/common/info";
    }
    /**
     * 设置超时标志，超时时会跳转到login
     */
    @RequestMapping("/timeout")
    public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase(
                "XMLHttpRequest")) { // ajax 超时处理  
            response.getWriter().print("timeout"); //设置超时标识
            response.getWriter().close();
        } else {
            response.sendRedirect("/login");
        }
    }
    /**
     * 特殊设备展示页面
     */
    @RequestMapping("/teshuInfo")
    public String teshuInfo(){
        return "/common/teshuInfo";
    }


}
