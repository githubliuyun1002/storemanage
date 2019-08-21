package com.xiabuxiabu.storemanage.security;

import com.xiabuxiabu.storemanage.entity.ccuser.CCPermission;
import com.xiabuxiabu.storemanage.entity.ccuser.CCRole;
import com.xiabuxiabu.storemanage.entity.user.Permission;
import com.xiabuxiabu.storemanage.entity.user.Role;
import org.springframework.security.core.userdetails.User;
import com.xiabuxiabu.storemanage.repository.user.PermissionRepository;
import com.xiabuxiabu.storemanage.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private static Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    //对湊湊的人员信息进行调整

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.xiabuxiabu.storemanage.entity.user.User user = userRepository.findByName(username);
        if(user!=null){

            List<Permission> permissions = new ArrayList<>();
            for(Role role : user.getRoles()){
               for (Permission permission : role.getPermissions()){
                   permissions.add(permission);
               }
            }
            List<CCPermission> ccPermissions = new ArrayList<>();
            for(CCRole ccRole : user.getCcRoleSet()){
                for(CCPermission ccPermission : ccRole.getCcPermissions()){
                    ccPermissions.add(ccPermission);
                }
            }

            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for(Permission permission : permissions){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                grantedAuthorities.add(grantedAuthority);
            }
            for (CCPermission ccPermission : ccPermissions){
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(ccPermission.getName());
                grantedAuthorities.add(grantedAuthority);
            }
            return new User(user.getUsername(),user.getPassword(),grantedAuthorities);
        }else{
            throw new  UsernameNotFoundException("admin: "+username + " do not exist!");
        }

        /*湊湊人员信息*/




    }
}
