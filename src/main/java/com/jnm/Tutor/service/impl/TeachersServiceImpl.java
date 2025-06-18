package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.mapper.TeachersMapper;
import com.jnm.Tutor.model.Teachers;
import com.jnm.Tutor.service.TeachersService;
import org.springframework.stereotype.Service;

@Service
public class TeachersServiceImpl extends ServiceImpl<TeachersMapper, Teachers> implements TeachersService {
}