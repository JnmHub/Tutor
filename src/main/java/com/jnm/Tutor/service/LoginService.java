package com.jnm.Tutor.service;

import org.springframework.security.core.AuthenticationException;

import com.jnm.Tutor.model.vo.User;

public interface LoginService {
    User loadByAccountType(String account, String userType,String openId) throws AuthenticationException;
    User loadByAccount(String account,String openId);
    User loadById(String id, String userType);
    User loadByOpenId(String openId, String userType);
}
