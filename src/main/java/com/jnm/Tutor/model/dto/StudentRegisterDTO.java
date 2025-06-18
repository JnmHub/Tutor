package com.jnm.Tutor.model.dto;

import com.jnm.Tutor.model.enums.EducationSystemEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class StudentRegisterDTO {

    @NotBlank(message = "头像不能为空")
    private String avatar;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于0")
    private Integer age;

    @NotNull(message = "年级不能为空")
    @Min(value = 1, message = "年级必须大于0")
    private Integer grade;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotBlank(message = "登录账号不能为空")
    private String account;

    @NotBlank(message = "登录密码不能为空")
    private String password;

    @NotNull(message = "学制不能为空")
    private EducationSystemEnum educationSystem;

    @Valid // 嵌套验证，确保List中的每个对象都符合验证规则
    @NotEmpty(message = "至少需要一门科目成绩")
    private List<StudentScoreDTO> scores;
}