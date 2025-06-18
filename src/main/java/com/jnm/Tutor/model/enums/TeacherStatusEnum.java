package com.jnm.Tutor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 教师状态枚举
 */
@Getter
public enum TeacherStatusEnum {
    PENDING("pending", "待审核"),
    APPROVED("approved", "正常"),
    BANNED("banned", "禁用");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    TeacherStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}