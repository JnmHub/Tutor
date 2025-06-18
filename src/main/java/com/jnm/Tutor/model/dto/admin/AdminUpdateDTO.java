package com.jnm.Tutor.model.dto.admin;

import lombok.Data;

@Data
public class AdminUpdateDTO {
    // 更新时，姓名和密码都不是必填项
    private String name;
    private String password;
}