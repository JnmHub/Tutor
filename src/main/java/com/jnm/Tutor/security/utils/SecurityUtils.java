package com.jnm.Tutor.security.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jnm.Tutor.exception.ServerException;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.model.vo.User;
import com.jnm.Tutor.security.token.JwtAuthenticationToken;

@UtilityClass
public class SecurityUtils {
    public User getCurrentUser() {
        try{
            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            return (User) authentication.getPrincipal();
        } catch (Exception e) {
            throw new ServerException(ErrorEnum.NOT_LOGIN);
        }

    }
}
