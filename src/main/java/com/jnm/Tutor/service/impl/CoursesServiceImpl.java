package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.mapper.CoursesMapper;
import com.jnm.Tutor.model.Courses;
import com.jnm.Tutor.model.Enrollments;
import com.jnm.Tutor.model.dto.course.CourseDTO;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.security.utils.SecurityUtils;
import com.jnm.Tutor.service.CoursesService;
import com.jnm.Tutor.service.EnrollmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class CoursesServiceImpl extends ServiceImpl<CoursesMapper, Courses> implements CoursesService {
    @Autowired
    @Lazy
    private EnrollmentsService enrollmentsService;

    @Override
    public void createCourse(CourseDTO courseDTO) {
        String teacherId = SecurityUtils.getCurrentUser().getId();
        Courses course = new Courses();
        course.setTeacherId(teacherId);
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setSubject(courseDTO.getSubject());
        course.setPrice(courseDTO.getPrice());
        course.setIsDeleted(false); // 默认未删除
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        this.save(course);
    }

    @Override
    public void updateCourse(String courseId, CourseDTO courseDTO) {
        Courses course = getCourseAndCheckOwnership(courseId);
        course.setTitle(courseDTO.getTitle());
        course.setDescription(courseDTO.getDescription());
        course.setSubject(courseDTO.getSubject());
        course.setPrice(courseDTO.getPrice());
        course.setUpdatedAt(LocalDateTime.now());
        this.updateById(course);
    }

    @Override
    public void deleteCourse(String courseId) {
        Courses course = getCourseAndCheckOwnership(courseId);
        // 检查该课程是否已被任何学生报名
        long enrollmentCount = enrollmentsService.count(new QueryWrapper<Enrollments>().eq("course_id", courseId));
        if (enrollmentCount > 0) {
            throw new ValidatedException(ErrorEnum.HAS_USED);
        }
        // 软删除
        course.setIsDeleted(true);
        course.setUpdatedAt(LocalDateTime.now());
        this.updateById(course);
    }

    @Override
    public IPage<Courses> getMyCourses(Page<Courses> page) {
        String teacherId = SecurityUtils.getCurrentUser().getId();
        return this.page(page, new QueryWrapper<Courses>()
                .eq("teacher_id", teacherId)
                .eq("is_deleted", false)
                .orderByDesc("updated_at"));
    }

    // 内部辅助方法，用于获取课程并校验是否属于当前登录的教师
    private Courses getCourseAndCheckOwnership(String courseId) {
        Courses course = this.getById(courseId);
        if (course == null || course.getIsDeleted()) {
            throw new ValidatedException(ErrorEnum.NOT_EXIST_ERROR);
        }
        String currentTeacherId = SecurityUtils.getCurrentUser().getId();
        if (!Objects.equals(course.getTeacherId(), currentTeacherId)) {
            throw new ValidatedException(ErrorEnum.NO_AUTHORITY);
        }
        return course;
    }
}