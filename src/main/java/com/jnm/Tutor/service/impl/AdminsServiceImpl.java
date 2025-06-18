package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.mapper.AdminsMapper;
import com.jnm.Tutor.model.Admins;
import com.jnm.Tutor.model.dto.admin.AdminCreateDTO;
import com.jnm.Tutor.model.dto.admin.AdminUpdateDTO;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.service.AdminsService;
import com.jnm.Tutor.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminsServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createAdmin(AdminCreateDTO dto) {
        // 检查账号是否已存在
        long count = this.count(new QueryWrapper<Admins>().eq("account", dto.getAccount()));
        if (count > 0) {
            throw new ValidatedException(ErrorEnum.HAS_ACCOUNT);
        }

        Admins admin = new Admins();
        admin.setAccount(dto.getAccount());
        admin.setName(dto.getName());
        admin.setPassword(passwordEncoder.encode(dto.getPassword())); // 加密密码
        admin.setCreatedAt(LocalDateTime.now());
        this.save(admin);
    }

    @Override
    public void updateAdmin(String id, AdminUpdateDTO dto) {
        Admins admin = this.getById(id);
        if(admin == null) {
            throw new ValidatedException(ErrorEnum.NOT_EXIST_ERROR);
        }
        // 按需更新字段
        if (!StringUtil.isNullOrEmpty(dto.getName())) {
            admin.setName(dto.getName());
        }
        if (!StringUtil.isNullOrEmpty(dto.getPassword())) {
            admin.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        this.updateById(admin);
    }
}