package com.jnm.Tutor.service;

import com.jnm.Tutor.model.dto.StudentRegisterDTO;
import com.jnm.Tutor.model.dto.TeacherRegisterDTO;

public interface UserService {
    /**
     * 注册新教师
     * @param teacherRegisterDTO 教师注册信息
     */
    void registerTeacher(TeacherRegisterDTO teacherRegisterDTO);

    /**
     * 注册新学生
     * @param studentRegisterDTO 学生注册信息及成绩
     */
    void registerStudent(StudentRegisterDTO studentRegisterDTO);
}