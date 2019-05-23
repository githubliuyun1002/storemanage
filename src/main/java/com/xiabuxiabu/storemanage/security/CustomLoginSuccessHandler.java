package com.xiabuxiabu.storemanage.security;


import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.repository.user.UserRepository;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/**
 * 登录成功进行处理
 */
@Service
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    Log logger = LogFactory.getLog(CustomLoginSuccessHandler.class);
    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //验证用户名
        User user = userRepository.findByName(authentication.getName());
        HttpSession  session = httpServletRequest.getSession();
        session.setAttribute("userName",user.getUsername());
        httpServletResponse.sendRedirect("/home");
    }
}
