package com.jnm.Tutor.security;


import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jnm.Tutor.security.filter.AccountPasswordAuthenticationFilter;
import com.jnm.Tutor.security.filter.JwtAuthenticationFilter;
import com.jnm.Tutor.security.filter.OpenIdAuthenticationFilter;
import com.jnm.Tutor.security.handler.UserAccessDeniedHandler;
import com.jnm.Tutor.security.handler.UserAuthenticationEntryPoint;
import com.jnm.Tutor.security.provider.AccountPasswordAuthenticationProvider;
import com.jnm.Tutor.security.provider.JwtAuthenticationProvider;
import com.jnm.Tutor.security.provider.OpenIdAuthenticationProvider;
import com.jnm.Tutor.service.LoginService;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    ObjectPostProcessor<Object> objectPostProcessor;
    @Resource
    UserAuthenticationEntryPoint entryPoint;
    @Resource
    UserAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private LoginService loginService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    protected AccountPasswordAuthenticationProvider accountPasswordAuthenticationProvider(){
        AccountPasswordAuthenticationProvider provider = new AccountPasswordAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        provider.setLoginService(loginService);
        return provider;
    }

    @Bean
    protected JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }
    @Bean
    protected OpenIdAuthenticationProvider openIdAuthenticationProvider(){
        return new OpenIdAuthenticationProvider();
    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        AuthenticationManagerBuilder managerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
        managerBuilder.authenticationProvider(accountPasswordAuthenticationProvider());
        managerBuilder.authenticationProvider(jwtAuthenticationProvider());
        managerBuilder.authenticationProvider(openIdAuthenticationProvider());
        return managerBuilder.build();
    }

    @Bean
    public AccountPasswordAuthenticationFilter accountPasswordAuthenticationFilter() throws Exception {
        AccountPasswordAuthenticationFilter filter = new AccountPasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
    @Bean
    public OpenIdAuthenticationFilter openIdAuthenticationFilter() throws Exception {
        OpenIdAuthenticationFilter filter = new OpenIdAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("*");
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://yogod.geekai.xyz:3000","https://yogod.geekai.xyz:3000"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.setAllowCredentials(true);//允许带凭证
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //cors跨域支持
        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);
        //放开一些接口的权限校验
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/wxlogin","/wx/**","/error", "/email/**","/users/register","/upload","/ws/**","/verify").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/.*").permitAll());
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/image/*").permitAll());

//        http.authorizeHttpRequests().requestMatchers("/error", "/verify", "/supplier","/department/paged","/department").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "/inventory").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET, "^/company/.{32}$").permitAll();
        //其余的都需授权访问
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
        //设置此项则不开启,表单登录可自定义登录接口，不走UsernamePasswordAuthenticationFilter
        http.formLogin(AbstractHttpConfigurer::disable);
        //添加自定义的过滤器
//        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        http.addFilterBefore(accountPasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(openIdAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //自定义无权限提示
        http.exceptionHandling(eh -> eh.authenticationEntryPoint(entryPoint).accessDeniedHandler(accessDeniedHandler));
        //全局不创建session
        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //禁用页面缓存，返回的都是json
//        http.headers().frameOptions().disable().cacheControl();
        http.headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable).cacheControl(Customizer.withDefaults()));
        return http.build();
    }
}
