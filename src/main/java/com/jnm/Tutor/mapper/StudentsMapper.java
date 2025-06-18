package com.jnm.Tutor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jnm.Tutor.model.Students;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentsMapper extends BaseMapper<Students> {
}