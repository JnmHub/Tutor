package com.jnm.Tutor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jnm.Tutor.model.Students;
import com.jnm.Tutor.model.dto.student.StudentUpdateDTO;
import com.jnm.Tutor.model.dto.student.StudentViewDTO;

public interface StudentsService extends IService<Students> {

    /**
     * 分页查询学生信息，并附带成绩
     * @param page 分页对象
     * @param name 学生姓名，用于模糊查询
     * @param phone 学生手机号，用于精确查询
     * @return 包含学生视图对象的分页结果
     */
    IPage<StudentViewDTO> getStudentPage(Page<Students> page, String name, String phone);

    /**
     * 更新学生信息
     * @param id 学生ID
     * @param dto 更新数据
     */
    void updateStudent(String id, StudentUpdateDTO dto);
}