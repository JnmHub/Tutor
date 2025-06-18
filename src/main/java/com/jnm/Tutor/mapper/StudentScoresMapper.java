package com.jnm.Tutor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jnm.Tutor.model.StudentScores;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentScoresMapper extends BaseMapper<StudentScores> {
}