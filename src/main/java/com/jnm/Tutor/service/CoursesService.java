package com.jnm.Tutor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jnm.Tutor.model.Courses;
import com.jnm.Tutor.model.dto.course.CourseDTO;

public interface CoursesService extends IService<Courses> {
    // 创建课程
    void createCourse(CourseDTO courseDTO);
    // 更新课程 (并校验权限)
    void updateCourse(String courseId, CourseDTO courseDTO);
    // 删除课程 (软删除，并校验权限和是否可删除)
    void deleteCourse(String courseId);
    // 获取当前教师自己的课程列表
    IPage<Courses> getMyCourses(Page<Courses> page);
}