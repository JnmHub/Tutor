package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.mapper.StudentsMapper;
import com.jnm.Tutor.model.StudentScores;
import com.jnm.Tutor.model.Students;
import com.jnm.Tutor.model.dto.StudentScoreDTO;
import com.jnm.Tutor.model.dto.student.StudentUpdateDTO;
import com.jnm.Tutor.model.dto.student.StudentViewDTO;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.service.StudentScoresService;
import com.jnm.Tutor.service.StudentsService;
import com.jnm.Tutor.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {

    @Autowired
    private StudentScoresService studentScoresService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public IPage<StudentViewDTO> getStudentPage(Page<Students> page, String name, String phone) {
        QueryWrapper<Students> queryWrapper = new QueryWrapper<>();
        // 添加查询条件
        if (!StringUtil.isNullOrEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if (!StringUtil.isNullOrEmpty(phone)) {
            queryWrapper.eq("phone", phone);
        }
        queryWrapper.orderByDesc("created_at");

        IPage<Students> studentPage = this.page(page, queryWrapper);

        // 转换DTO并查询每个学生的成绩
        return studentPage.convert(student -> {
            StudentViewDTO dto = new StudentViewDTO();
            BeanUtils.copyProperties(student, dto);

            // 查询并设置成绩
            List<StudentScores> scores = studentScoresService.list(new QueryWrapper<StudentScores>().eq("student_id", student.getId()));
            List<StudentScoreDTO> scoreDTOs = scores.stream().map(score -> {
                StudentScoreDTO scoreDto = new StudentScoreDTO();
                BeanUtils.copyProperties(score, scoreDto);
                return scoreDto;
            }).collect(Collectors.toList());
            dto.setScores(scoreDTOs);

            return dto;
        });
    }

    @Override
    public void updateStudent(String id, StudentUpdateDTO dto) {
        Students student = this.getById(id);
        if (student == null) {
            throw new ValidatedException(ErrorEnum.NOT_EXIST_ERROR);
        }

        // 使用BeanUtils选择性拷贝非空属性，或手动判断
        if (!StringUtil.isNullOrEmpty(dto.getAvatar())) student.setAvatar(dto.getAvatar());
        if (!StringUtil.isNullOrEmpty(dto.getName())) student.setName(dto.getName());
        if (dto.getAge() != null) student.setAge(dto.getAge());
        if (dto.getGrade() != null) student.setGrade(dto.getGrade());
        if (!StringUtil.isNullOrEmpty(dto.getPhone())) student.setPhone(dto.getPhone());
        if (!StringUtil.isNullOrEmpty(dto.getAddress())) student.setAddress(dto.getAddress());
        if (dto.getEducationSystem() != null) student.setEducationSystem(dto.getEducationSystem().getCode());
        if (dto.getStatus() != null) student.setStatus(dto.getStatus().getCode());

        // 如果密码不为空，则加密并更新
        if (!StringUtil.isNullOrEmpty(dto.getPassword())) {
            student.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        this.updateById(student);
    }
}