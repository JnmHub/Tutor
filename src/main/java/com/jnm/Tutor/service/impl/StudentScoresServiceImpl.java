package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.mapper.StudentScoresMapper;
import com.jnm.Tutor.model.StudentScores;
import com.jnm.Tutor.service.StudentScoresService;
import org.springframework.stereotype.Service;

@Service
public class StudentScoresServiceImpl extends ServiceImpl<StudentScoresMapper, StudentScores> implements StudentScoresService {
}