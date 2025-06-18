package com.jnm.Tutor.model.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum UserTypesEnum {
    ADMIN("admin","管理员"),
    TEACHER("teacher","教师"),
    STUDENT("student","学生");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    UserTypesEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
