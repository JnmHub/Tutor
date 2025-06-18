package com.jnm.Tutor.model.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminCreateDTO {
    @NotBlank(message = "登录账号不能为空")
    private String account;

    @NotBlank(message = "登录密码不能为空")
    private String password;

    @NotBlank(message = "姓名不能为空")
    private String name;
}