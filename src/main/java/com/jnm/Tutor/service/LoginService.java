package com.jnm.Tutor.service;

import com.jnm.Tutor.model.vo.User;
import org.springframework.security.core.AuthenticationException;

public interface LoginService {
    User loadByAccountType(String account, String userType) throws AuthenticationException;
    User loadById(String id, String userType);
    User loadByOpenId(String openId, String userType);
}
