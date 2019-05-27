package com.xiabuxiabu.storemanage.controller.user;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.entity.user.Role;

import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.entity.user.UserType;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.publicutil.PublicStatusService;
import com.xiabuxiabu.storemanage.service.user.RoleTypeService;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Controller
@RequestMapping("/person")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PublicStatusService publicStatusService;
    @Autowired
    private MarketService marketService;
    @RequestMapping("/findByUserName")
    @ResponseBody
    public User findByUserName(String userName){
       return userService.findByUserName(userName);
    }
    /**
     * 查询所有的人员列表
     */
    @RequestMapping("/findAll")
    @ResponseBody
    public Page<User> findAll(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return userService.findAll(page,pageSize,searchName);
    }
    /**
     * 查询所有的角色列表
     */
    @RequestMapping("/roleList")
    @ResponseBody
    public List<Role> RoleList(){
        return userService.RoleList();
    }

    @RequestMapping("/userTypeList")
    @ResponseBody
    public List<UserType> roleTypeList(){
        return userService.findAllRole();
    }
    /**
     * 查询所有的公共状态列表
     */
    @RequestMapping("/statusList")
    @ResponseBody
    public List<PublicStatus> statusList(){
        return publicStatusService.findAll();
    }
    /**
     * 展示数据的首页
     */
    @RequestMapping("/home")
    public String home(){
        return "/user/home";
    }
    /**
     * 新增保存人员,在列表中进行展示
     */
    @RequestMapping("/addSave")
    public String addUser(User user){
        System.out.println("新增人员----》"+user);
        String password = user.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        user.setPassword(password);
        userService.addSave(user);
        return "redirect:/person/home";
    }
    /**
     * 修改人员进行展示
     */
    @RequestMapping("/findById")
    @ResponseBody
    public User findById(int id){
        return  userService.findById(id);
    }
    /**
     * 保存修改的数据
     */
    @RequestMapping("/updateSave")
    public String updateSave(User user){
        System.out.println("执行修改动作user---->"+user);
        if(!user.getPassword().equals(userService.findById(user.getId()).getPassword())){
            //密码加密
            String password = user.getPassword();
            password = new BCryptPasswordEncoder().encode(password);
            user.setPassword(password);
        }
        userService.updateSave(user);
        return "redirect:/person/home";
    }
    @RequestMapping("/marketList")
    @ResponseBody
    public List<MarketEntity> marketList(){
        return marketService.findAll();
    }


}
