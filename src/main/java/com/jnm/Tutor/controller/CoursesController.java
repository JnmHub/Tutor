package com.jnm.Tutor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnm.Tutor.model.Courses;
import com.jnm.Tutor.model.dto.course.CourseDTO;
import com.jnm.Tutor.service.CoursesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "课程管理 (教师端)")
@RestController
@RequestMapping("/courses")
@Secured("ROLE_TEACHER")
public class CoursesController {

    @Autowired
    private CoursesService coursesService;

    @Operation(summary = "发布新课程")
    @PostMapping
    public void create(@Valid @RequestBody CourseDTO courseDTO) {
        coursesService.createCourse(courseDTO);
    }

    @Operation(summary = "更新我的课程")
    @PutMapping("/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody CourseDTO courseDTO) {
        coursesService.updateCourse(id, courseDTO);
    }

    @Operation(summary = "删除我的课程 (软删除)")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        coursesService.deleteCourse(id);
    }

    @Operation(summary = "获取我发布的课程列表")
    @GetMapping("/my")
    public IPage<Courses> getMyCourses(@RequestParam(defaultValue = "1") int current,
                                       @RequestParam(defaultValue = "10") int size) {
        Page<Courses> page = new Page<>(current, size);
        return coursesService.getMyCourses(page);
    }
}