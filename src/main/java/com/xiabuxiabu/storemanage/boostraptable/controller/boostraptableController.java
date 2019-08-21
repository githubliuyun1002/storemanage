package com.xiabuxiabu.storemanage.boostraptable.controller;

import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/table")
public class boostraptableController {
    @Autowired
    private UserService userService;
    @RequestMapping("/index")
    public String index(){
        return "/boostraptable/userpage";
    }
    @RequestMapping("/findAll")
    @ResponseBody
    public Map<String,Object> findAll(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){

        System.out.println("接受得参数页码-----》"+page+",页面大小-----》"+pageSize+",查询参数----》"+searchName);

        Page<User> pageList = userService.findAll(page,pageSize,searchName);

        Map<String,Object> map = new HashMap<>();
        List<User> content = pageList.getContent();
        //bootstrap-table要求服务器返回的json数据必须包含：total，rows两个节点
        //返回的参数total应该传递的是，页面有符合条件的共有多少条记录
        map.put("rows",content);
        map.put("total",pageList.getTotalElements());
        return map;
    }





}
