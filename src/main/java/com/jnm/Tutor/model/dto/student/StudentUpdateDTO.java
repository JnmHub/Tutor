package com.jnm.Tutor.model.dto.student;

import com.jnm.Tutor.model.enums.CommonStatusEnum;
import com.jnm.Tutor.model.enums.EducationSystemEnum;
import lombok.Data;

@Data
public class StudentUpdateDTO {
    // 更新时，所有字段都是可选的
    private String avatar;
    private String name;
    private Integer age;
    private Integer grade;
    private String phone;
    private String address;
    private String password; // 允许管理员重置密码
    private EducationSystemEnum educationSystem;
    private CommonStatusEnum status;
}