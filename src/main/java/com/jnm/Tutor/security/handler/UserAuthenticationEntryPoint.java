package com.jnm.Tutor.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.jnm.Tutor.controller.result.Result;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.util.ResponseUtil;

import java.io.IOException;


@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Result result = Result.fail(ErrorEnum.NOT_LOGIN.getCode(), ErrorEnum.NOT_LOGIN.getMsg());
        ResponseUtil.outOfJson(response, result);
    }
}
