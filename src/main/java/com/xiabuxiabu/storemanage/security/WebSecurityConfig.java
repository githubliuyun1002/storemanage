package com.xiabuxiabu.storemanage.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.DefaultWebInvocationPrivilegeEvaluator;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity    //开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
    private static Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;  //客户端登录成功的处理器
    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.headers().frameOptions().disable();
         http.sessionManagement().invalidSessionUrl("/timeout");
        http.authorizeRequests().antMatchers("/publicUtils/**", "/**/*.css", "/**/*.js", "*/*/img/*", "/", "/**/*.txt").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")  //定义当需要用户登录时候，转到的登录页面
                .successHandler(authenticationSuccessHandler)
                //.defaultSuccessUrl("/index")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/j_spring_security_logout"))
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll();
    }
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //密码设置
        LOGGER.info("登陆验证开始");
        auth.userDetailsService(this.myUserDetailsService).passwordEncoder(new BCryptPasswordEncoder() {
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.privilegeEvaluator(new DefaultWebInvocationPrivilegeEvaluator(myFilterSecurityInterceptor));
    }
}
