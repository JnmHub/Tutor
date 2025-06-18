package com.jnm.Tutor.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.jnm.Tutor.controller.result.Result;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.util.ResponseUtil;

import java.io.IOException;


@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Result result = Result.fail(ErrorEnum.NO_AUTHORITY.getCode(), ErrorEnum.NO_AUTHORITY.getMsg());
        ResponseUtil.outOfJson(response, result);
    }
}
