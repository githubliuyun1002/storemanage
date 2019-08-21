package com.xiabuxiabu.storemanage.controller.ccuser;

import com.xiabuxiabu.storemanage.entity.ccuser.CCRole;
import com.xiabuxiabu.storemanage.entity.ccuser.CCUserType;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.service.ccuser.CCRoleService;
import com.xiabuxiabu.storemanage.service.ccuser.CCUserService;
import com.xiabuxiabu.storemanage.service.ccuser.CCUserTypeServive;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Controller
@RequestMapping("/ccperson")
public class CCUserController {
    @Autowired
    private CCUserService ccUserService;
    @Autowired
    private CCRoleService ccRoleService;

    @Autowired
    private CCUserTypeServive ccUserTypeServive;

    @Autowired
    private CCUserService userService;

    /*
    人员的展示页面
     */
    @RequestMapping("/home")
    public String home(){
        return "/ccuser/user";
    }
    @RequestMapping("/findAll")
    @ResponseBody
    public Page<User> findAll(int page,int pageSize,String searchName){
        return ccUserService.findAll(page,pageSize,searchName);
    }

    /**
     * 人员的角色类型
     * @return
     */
    @RequestMapping("/roleList")
    @ResponseBody
    public List<CCRole> roleList(){
         return  ccRoleService.find();
    }

    /**
     *加载人员类型
     */
    @RequestMapping("/userTypeList")
    @ResponseBody
    public List<CCUserType> findAll(){
        return ccUserTypeServive.findAll();
    }

    /**
     * 新增人员
     * @param user
     * @return
     */
    @RequestMapping("/addSave")
    public String addSave(User user){
        //在对特殊设备进行管理时，特殊设备的人员需要填加标记为特殊
        user.setSign("teshu");
        String password = user.getPassword();
        password = new BCryptPasswordEncoder().encode(password);
        user.setPassword(password);
        //System.out.println("user------>"+user);
        ccUserService.save(user);
        return "redirect:/ccperson/home";
    }
    /**
     * 修改人员
     */
    @RequestMapping("/updateSave")
    public String updateSave(User user){
        int userId = user.getId();
        //当两次密码不一致时，对密码进行修改，然后需要加密保存
        if(!user.getPassword().equals(ccUserService.findById(user.getId()).getPassword())){
            //密码加密
            String password = user.getPassword();
            password = new BCryptPasswordEncoder().encode(password);
            user.setPassword(password);
        }
        if(ccUserService.findById(userId)!=null){
             User userDB = ccUserService.findById(userId);
             //门店人员的标记
             user.setSign(userDB.getSign());
             System.out.println("修改的页面值user------>"+user);
             ccUserService.save(user);
        }
        return "redirect:/ccperson/home";

    }
    @RequestMapping("/findById")
    @ResponseBody
    public User findById(int id){

        return  ccUserService.findById(id);
    }
   /* @RequestMapping("/findByUser")
    @ResponseBody
    public List<User> findByUser(String market){
        return   userService.ccfindByMarketName(market);
    }
    //http://localhost:8090/ccperson/findByUser?market=%27%E6%B9%8A%E6%B9%8A%27*/





}
