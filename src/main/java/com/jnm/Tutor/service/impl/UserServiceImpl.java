package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.model.StudentScores;
import com.jnm.Tutor.model.Students;
import com.jnm.Tutor.model.Teachers;
import com.jnm.Tutor.model.dto.StudentRegisterDTO;
import com.jnm.Tutor.model.dto.TeacherRegisterDTO;
import com.jnm.Tutor.model.enums.CommonStatusEnum;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.model.enums.TeacherStatusEnum;
import com.jnm.Tutor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TeachersService teachersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private StudentsService studentsService;

    @Autowired
    private StudentScoresService studentScoresService;

    @Override
    @Transactional // 开启事务，确保操作的原子性
    public void registerTeacher(TeacherRegisterDTO dto) {
        // 1. 检查账号是否已存在
        long count = teachersService.count(new QueryWrapper<Teachers>().eq("account", dto.getAccount()));
        if (count > 0) {
            throw new ValidatedException(ErrorEnum.HAS_ACCOUNT);
        }

        // 2. 创建并填充 Teachers 实体
        Teachers teacher = new Teachers();
        teacher.setAccount(dto.getAccount());
        teacher.setName(dto.getName());
        // 3. 对密码进行加密
        teacher.setPassword(passwordEncoder.encode(dto.getPassword()));
        teacher.setIdCardPhoto(dto.getIdCardPhoto());
        // 4. 设置初始状态为“待审核”
        teacher.setStatus(TeacherStatusEnum.PENDING.getCode());
        teacher.setCreatedAt(LocalDateTime.now());
        // 设置评分初始值
        teacher.setAvgTeachingAttitudeScore(BigDecimal.ZERO);
        teacher.setAvgServiceAttitudeScore(BigDecimal.ZERO);

        // 5. 保存教师信息到数据库
        teachersService.save(teacher);


    }

    @Override
    @Transactional // 开启事务
    public void registerStudent(StudentRegisterDTO dto) {
        // 1. 检查账号或手机号是否已存在
        long count = studentsService.count(new QueryWrapper<Students>()
                .eq("account", dto.getAccount())
                .or()
                .eq("phone", dto.getPhone()));
        if (count > 0) {
            throw new ValidatedException(205, "账号或手机号已存在");
        }

        // 2. 创建并保存学生基本信息
        Students student = new Students();
        student.setAccount(dto.getAccount());
        student.setPassword(passwordEncoder.encode(dto.getPassword()));
        student.setName(dto.getName());
        student.setAge(dto.getAge());
        student.setGrade(dto.getGrade());
        student.setPhone(dto.getPhone());
        student.setAddress(dto.getAddress());
        // TODO 看情况，先写着
        student.setAvatar(dto.getAvatar());
        student.setEducationSystem(dto.getEducationSystem().getCode());
        student.setStatus(CommonStatusEnum.ACTIVE.getCode()); // 学生注册后默认状态为 active
        student.setCreatedAt(LocalDateTime.now());
        studentsService.save(student); // 保存后，student对象的id字段会被自动填充

        // 3. 准备并批量保存学生成绩
        List<StudentScores> scoresList = dto.getScores().stream().map(scoreDto -> {
            StudentScores studentScore = new StudentScores();
            studentScore.setStudentId(student.getId()); // 关联刚刚创建的学生ID
            studentScore.setSubjectName(scoreDto.getSubjectName());
            studentScore.setScore(scoreDto.getScore());
            return studentScore;
        }).collect(Collectors.toList());
        studentScoresService.saveBatch(scoresList); // 批量插入成绩

    }
}