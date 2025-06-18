package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnm.Tutor.model.Admins;
import com.jnm.Tutor.model.Students;
import com.jnm.Tutor.model.Teachers;
import com.jnm.Tutor.model.enums.CommonStatusEnum;
import com.jnm.Tutor.model.enums.TeacherStatusEnum;
import com.jnm.Tutor.model.enums.UserTypesEnum;
import com.jnm.Tutor.model.vo.User;
import com.jnm.Tutor.service.AdminsService;
import com.jnm.Tutor.service.LoginService;
import com.jnm.Tutor.service.StudentsService;
import com.jnm.Tutor.service.TeachersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@CacheConfig(cacheNames = {"user"})
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AdminsService adminsService;

    @Autowired
    private TeachersService teachersService;

    @Autowired
    private StudentsService studentsService;

    @CachePut(key = "#result.id")
    @Override
    public User loadByAccountType(String account, String userType) throws AuthenticationException {
        if (Objects.equals(userType, UserTypesEnum.ADMIN.getCode())) {
            Admins admin = adminsService.getOne(new QueryWrapper<Admins>().eq("account", account));
            if (admin == null) {
                throw new UsernameNotFoundException("用户名或密码错误");
            }
            String finalUserType = determineFinalUserType(admin.getId(), userType);
            return new User(admin.getId(), admin.getAccount(), admin.getName(), admin.getPassword(), finalUserType, listUserPermissions(finalUserType));
        } else if (Objects.equals(userType, UserTypesEnum.TEACHER.getCode())) {
            Teachers teacher = teachersService.getOne(new QueryWrapper<Teachers>().eq("account", account));
            if (teacher == null) {
                throw new UsernameNotFoundException("用户名或密码错误");
            }
            if (Objects.equals(teacher.getStatus(), TeacherStatusEnum.PENDING.getCode())) {
                throw new DisabledException("您的教师账号正在审核中，请耐心等待");
            }
            if (Objects.equals(teacher.getStatus(), TeacherStatusEnum.BANNED.getCode())) {
                throw new DisabledException("您的教师账号已被禁用，请联系管理员");
            }
            String finalUserType = determineFinalUserType(teacher.getId(), userType);
            return new User(teacher.getId(), teacher.getAccount(), teacher.getName(), teacher.getPassword(), finalUserType, listUserPermissions(finalUserType));
        } else if (Objects.equals(userType, UserTypesEnum.STUDENT.getCode())) {
            Students student = studentsService.getOne(new QueryWrapper<Students>().eq("account", account));
            if (student == null) {
                throw new UsernameNotFoundException("用户名或密码错误");
            }
            if (Objects.equals(student.getStatus(), CommonStatusEnum.BANNED.getCode())) {
                throw new DisabledException("您的学生账号已被禁用，请联系管理员");
            }
            String finalUserType = determineFinalUserType(student.getId(), userType);
            return new User(student.getId(), student.getAccount(), student.getName(), student.getPassword(), finalUserType, listUserPermissions(finalUserType));
        } else {
            throw new UsernameNotFoundException("不支持的用户类型: " + userType);
        }
    }



    @Cacheable(key = "#id")
    @Override
    public User loadById(String id, String userType) {
        String finalUserType = determineFinalUserType(id, userType);

        if (Objects.equals(userType, UserTypesEnum.ADMIN.getCode())) {
            Admins admin = adminsService.getById(id);
            if (admin != null) {
                return new User(admin.getId(), admin.getAccount(), admin.getName(), admin.getPassword(), finalUserType, listUserPermissions(finalUserType));
            }
        } else if (Objects.equals(userType, UserTypesEnum.TEACHER.getCode())) {
            Teachers teacher = teachersService.getById(id);
            if (teacher != null) {
                boolean isEnabled = TeacherStatusEnum.APPROVED.getCode().equals(teacher.getStatus());
                return new User(teacher.getId(), teacher.getAccount(), teacher.getName(), teacher.getPassword(), finalUserType, listUserPermissions(finalUserType), isEnabled);
            }
        } else if (Objects.equals(userType, UserTypesEnum.STUDENT.getCode())) {
            Students student = studentsService.getById(id);
            if (student != null) {
                boolean isEnabled = CommonStatusEnum.ACTIVE.getCode().equals(student.getStatus());
                return new User(student.getId(), student.getAccount(), student.getName(), student.getPassword(), finalUserType, listUserPermissions(finalUserType), isEnabled);
            }
        }
        return null;
    }

    @CachePut(key = "#result.id")
    @Override
    public User loadByOpenId(String openId, String userType) {
        return null;
    }

    /**
     * 根据用户类型字符串生成权限列表
     * @param userType 用户类型
     * @return 权限集合
     */
    private List<GrantedAuthority> listUserPermissions(String userType) {
        List<GrantedAuthority> list = new ArrayList<>();
        // 赋予其本身的角色
        list.add(new SimpleGrantedAuthority("ROLE_" + userType.toUpperCase()));

        // 如果是超级管理员，额外赋予普通管理员的角色
        if ("SUPER".equals(userType)) {
            list.add(new SimpleGrantedAuthority("ROLE_" + UserTypesEnum.ADMIN.getCode().toUpperCase()));
        }

        return list;
    }

    /**
     * 根据用户ID判断是否为超级用户，并返回最终的用户类型
     * @param userId 用户的ID
     * @param originalUserType 原始的用户类型
     * @return 最终的用户类型 ("SUPER" 或 原始类型)
     */
    private String determineFinalUserType(String userId, String originalUserType) {
        if (userId != null && userId.contains("SUPER")) {
            return "SUPER";
        }
        return originalUserType;
    }
}