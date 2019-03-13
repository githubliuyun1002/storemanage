package com.xiabuxiabu.storemanage.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * 自定义过滤器与验证类
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
       if(null == collection || collection.size()<=0){
          return;
       }
       ConfigAttribute c;
       String needRole;
       for(Iterator<ConfigAttribute> iter = collection.iterator();iter.hasNext();){
           c = iter.next();
           needRole = c.getAttribute();
           for (GrantedAuthority ga:authentication.getAuthorities()){
               if(needRole.trim().equals(ga.getAuthority())){
                   return;
               }
           }
       }
       throw new AccessDeniedException("no right");


    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
