package com.jnm.Tutor.service.impl;

import com.jnm.Tutor.model.vo.User;
import com.jnm.Tutor.service.LoginService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames = {"user"})
@Service
public class LoginServiceImpl implements LoginService {



    @CachePut(key = "#result.id")
    @Override
    public User loadByAccountType(String account, String userType, String openId) throws AuthenticationException {
       return null;
    }

    @Override
    @CachePut(key = "#result.id")
    public User loadByAccount(String account,String openId) {
        return null;
    }

    @Cacheable(key = "#id")
    @Override
    public User loadById(String id, String userType) {
        return null;
    }

    @CachePut(key = "#result.id")
    @Override
    public User loadByOpenId(String openId, String userType) {
        return null;
    }

    private List<GrantedAuthority> listUserPermissions(String userType) {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_" + userType));
        return list;
    }
    
}
