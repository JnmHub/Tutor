package com.jnm.Tutor.controller;

import com.jnm.Tutor.controller.result.Result;
import com.jnm.Tutor.model.dto.StudentRegisterDTO;
import com.jnm.Tutor.model.dto.TeacherRegisterDTO;
import com.jnm.Tutor.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "用户（管理员、教师、学生）模块")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation( summary = "教师注册")
    @PostMapping("/register/teacher")
    public Result registerTeacher(@Valid @RequestBody TeacherRegisterDTO teacherRegisterDTO) {
        userService.registerTeacher(teacherRegisterDTO);
        return Result.success();
    }
    @Operation( summary = "学生注册")
    @PostMapping("/register/student")
    public Result registerStudent(@Valid @RequestBody StudentRegisterDTO studentRegisterDTO) {
        userService.registerStudent(studentRegisterDTO);
        return Result.success();
    }
}