package com.jnm.Tutor.config;

import com.jnm.Tutor.controller.result.DataResult;
import com.jnm.Tutor.controller.result.Result;
import com.jnm.Tutor.util.JSONUtil;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;
@Hidden
@RestControllerAdvice(basePackages = "com.jnm.Tutor.controller")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 判定哪些请求要执行beforeBodyWrite
     * @param methodParameter 请求执行的方法
     * @param converterType 信息转化类
     * @return 是否执行
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果方法上被 @NoWrapper 注解标记，则不进行包装，直接返回false
        if (methodParameter.getMethod().isAnnotationPresent(NoWrapper.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //无返回值，返回成功
        if (body == null) {
            return Result.success();
        }
        //返回为资源或已封装则直接返回
        if (body instanceof Resource || body instanceof Result) {
            return body;
        }
        if (body instanceof String) {
            return JSONUtil.toJSONString(new DataResult<>(body));
        }
        if ("error".equals(methodParameter.getMethod().getName())) {
            Map<String, Object> map = (Map<String, Object>) body;
            return Result.fail((int) map.get("status"), map.get("error") + ":" + map.get("message"));
        }
        return new DataResult<>(body);
    }
}
