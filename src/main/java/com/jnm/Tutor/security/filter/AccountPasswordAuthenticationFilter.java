package com.jnm.Tutor.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jnm.Tutor.cache.CustomCacheManager;
import com.jnm.Tutor.controller.result.DataResult;
import com.jnm.Tutor.controller.result.Result;
import com.jnm.Tutor.exception.VerifyException;
import com.jnm.Tutor.model.vo.User;
import com.jnm.Tutor.security.token.AccountPasswordAuthenticationToken;
import com.jnm.Tutor.util.ResponseUtil;
import com.jnm.Tutor.util.StringUtil;
import com.jnm.Tutor.util.TokenUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AccountPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_ACCOUNT_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    public static final String SPRING_SECURITY_FORM_USERTYPE_KEY = "userType";
    public static final String SPRING_SECURITY_FORM_VERIFY_KEY_KEY = "verifyKey";
    public static final String SPRING_SECURITY_FORM_VERIFY_CODE_KEY = "verifyCode";
    public static final String SPRING_SECURITY_FORM_OPEN_ID = "openId";
    @Autowired
    CustomCacheManager cacheManager;
    private String accountParameter = SPRING_SECURITY_FORM_ACCOUNT_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;
    private String userTypeParameter = SPRING_SECURITY_FORM_USERTYPE_KEY;
    private String verifyKeyParameter = SPRING_SECURITY_FORM_VERIFY_KEY_KEY;
    private String verifyCodeParameter = SPRING_SECURITY_FORM_VERIFY_CODE_KEY;
    private String openIdParameter = SPRING_SECURITY_FORM_OPEN_ID;
    private boolean postOnly = true;

    public AccountPasswordAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("请求方式不支持，支持POST方式" + request.getMethod());
        }
        BufferedReader reader = request.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        String jsonData = requestBody.toString();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = mapper.readValue(jsonData, Map.class);
        String username = obtainAccount(data);
        String password = obtainPassword(data);
        String userType = obtainUserType(data);
        String verifyKey = obtainVerifyKey(data);
        String verifyCode = obtainVerifyCode(data);
        String openId = obtainOpenIdParameter(data);
        if (userType == null) {
            userType = "";
        }
        AccountPasswordAuthenticationToken authRequest;
        if (StringUtil.isNullOrEmpty(username)) {
            throw new VerifyException("用户名不可为空");
        }
        if (StringUtil.isNullOrEmpty(password)) {
            throw new VerifyException("密码不可为空");
        }
        if (StringUtil.isNullOrEmpty(verifyKey) || StringUtil.isNullOrEmpty(verifyCode)) {
            throw new VerifyException("验证码不能为空");
        }
        Cache cache = cacheManager.getCache("verifyImg");
        if (cache != null) {
            String cachedCode = cache.get(verifyKey, String.class);
            if (cachedCode == null || !cachedCode.equalsIgnoreCase(verifyCode)) {
                throw new VerifyException("验证码错误");
            }
            cache.evict(verifyKey);
        }
        authRequest = new AccountPasswordAuthenticationToken(
                username, password, userType,openId);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
        User user = (User) authResult.getPrincipal();
        String token;
        token = TokenUtil.createToken(user.getId(), user.getName(), user.getUserType());
        Map<String, Object> data = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("name", user.getName());
        data.put("token", token);
        data.put("user", map);
        data.put("roles", user.getUserType());
        data.put("tokenExpiresIn", TokenUtil.tokenExpiration);
        data.put("tokenFreeTimeout", TokenUtil.tokenFreeTimeout);
        DataResult<Map<String, Object>> result = new DataResult<>(data);
        ResponseUtil.outOfJson(response, result);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
//        response.setStatus(HttpStatus.BAD_REQUEST.value());
        Result result = Result.fail(103, failed.getMessage());
        ResponseUtil.outOfJson(response, result);
    }

    @Nullable
    protected String obtainAccount(Map<String, String> request) {
        return request.get(this.accountParameter);
    }

    @Nullable
    protected String obtainPassword(Map<String, String> request) {
        return request.get(this.passwordParameter);
    }

    protected String obtainUserType(Map<String, String> request) {
        return request.get(this.userTypeParameter);
    }

    protected String obtainVerifyKey(Map<String, String> request) {
        return request.get(this.verifyKeyParameter);
    }

    protected String obtainVerifyCode(Map<String, String> request) {
        return request.get(this.verifyCodeParameter);
    }
    protected String obtainOpenIdParameter(Map<String, String> request) {
        return request.get(this.openIdParameter);
    }


    protected void setDetails(HttpServletRequest request, AccountPasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getAccountParameter() {
        return this.accountParameter;
    }

    public void setAccountParameter(String accountParameter) {
        Assert.hasText(accountParameter, "Account parameter must not be empty or null");
        this.accountParameter = accountParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }

    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public final String getUserTypeParameter() {
        return this.userTypeParameter;
    }

    public void setUserTypeParameter(String userTypeParameter) {
        Assert.hasText(userTypeParameter, "User type parameter must not be empty or null");
        this.userTypeParameter = userTypeParameter;
    }

    public final String getVerifyKeyParameter() {
        return this.verifyKeyParameter;
    }

    public void setVerifyKeyParameter(String verifyKeyParameter) {
        Assert.hasText(verifyKeyParameter, "Verify key parameter must not be empty or null");
        this.verifyKeyParameter = verifyKeyParameter;
    }

    public final String getVerifyCodeParameter() {
        return this.verifyCodeParameter;
    }

    public void setVerifyCodeParameter(String verifyCodeParameter) {
        Assert.hasText(verifyCodeParameter, "verify code parameter must not be empty or null");
        this.verifyCodeParameter = verifyCodeParameter;
    }
}
