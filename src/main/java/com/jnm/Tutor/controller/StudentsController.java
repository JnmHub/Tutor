package com.jnm.Tutor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnm.Tutor.model.Students;
import com.jnm.Tutor.model.dto.student.StudentUpdateDTO;
import com.jnm.Tutor.model.dto.student.StudentViewDTO;
import com.jnm.Tutor.service.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
@Tag(name = "学生管理模块")
@RestController
@RequestMapping("/students")
@Secured({"ROLE_ADMIN", "ROLE_SUPER"}) // <-- 权限控制：允许ADMIN和SUPER访问
public class StudentsController {

    @Autowired
    private StudentsService studentsService;

    // 分页查询学生列表，支持按姓名和手机号搜索、
    @Operation( summary = "分页查询学生列表")
    @GetMapping
    public IPage<StudentViewDTO> list(@RequestParam(defaultValue = "1") int current,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String phone) {
        Page<Students> page = new Page<>(current, size);
        return studentsService.getStudentPage(page, name, phone);
    }

    // 更新学生信息
    @Operation( summary = "更新学生信息")
    @PutMapping("/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody StudentUpdateDTO updateDTO) {
        studentsService.updateStudent(id, updateDTO);
    }

    // 删除学生 (逻辑删除，通过更新状态实现)
    @Operation( summary = "删除学生")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        // 这里我们通过调用更新接口，将学生状态置为“禁用”来实现逻辑删除
        StudentUpdateDTO updateDTO = new StudentUpdateDTO();
        updateDTO.setStatus(com.jnm.Tutor.model.enums.CommonStatusEnum.BANNED);
        studentsService.updateStudent(id, updateDTO);
    }
}