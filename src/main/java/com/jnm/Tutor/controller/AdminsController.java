package com.jnm.Tutor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnm.Tutor.controller.result.Result;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.model.Admins;
import com.jnm.Tutor.model.dto.admin.AdminCreateDTO;
import com.jnm.Tutor.model.dto.admin.AdminUpdateDTO;
import com.jnm.Tutor.model.dto.admin.AdminViewDTO;
import com.jnm.Tutor.security.utils.SecurityUtils;
import com.jnm.Tutor.service.AdminsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理员管理模块 (SUPER)")
@RestController
@RequestMapping("/admins")
@Secured("ROLE_SUPER") // <--- 关键：整个Controller都需要 "ROLE_SUPER" 权限
public class AdminsController {

    @Autowired
    private AdminsService adminsService;

    // 新增管理员
    @Operation(summary = "新增管理员") // <--- 新增：描述接口功能
    @PostMapping
    public Result create(@Valid @RequestBody AdminCreateDTO createDTO) {
        adminsService.createAdmin(createDTO);
        return Result.success();
    }

    // 分页查询管理员列表
    @Operation(summary = "分页查询管理员列表") // <--- 分页查询管理员列表：描述接口功能
    @GetMapping
    public IPage<AdminViewDTO> list(@RequestParam(defaultValue = "1") int current,
                                    @RequestParam(defaultValue = "10") int size) {
        Page<Admins> page = new Page<>(current, size);
        IPage<Admins> adminPage = adminsService.page(page);
        // 将 IPage<Admins> 转换为 IPage<AdminViewDTO> 以隐藏密码
        return adminPage.convert(admin -> {
            AdminViewDTO dto = new AdminViewDTO();
            BeanUtils.copyProperties(admin, dto);
            return dto;
        });
    }

    // 更新管理员
    @Operation(summary = "更新管理员") // <--- 更新管理员：描述接口功能
    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @Valid @RequestBody AdminUpdateDTO updateDTO) {
        adminsService.updateAdmin(id, updateDTO);
        return Result.success();
    }

    // 删除管理员
    @Operation(summary = "删除管理员") // <--- 删除管理员：描述接口功能
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id) {
        // 安全检查：防止超级管理员删除自己
        if (SecurityUtils.getCurrentUser().getId().equals(id)) {
            throw new ValidatedException(-1, "不能删除自己");
        }
        adminsService.removeById(id);
        return Result.success();
    }
}