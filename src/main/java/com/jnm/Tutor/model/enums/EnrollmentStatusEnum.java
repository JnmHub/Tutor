package com.jnm.Tutor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 报课学习状态枚举
 */
@Getter
public enum EnrollmentStatusEnum {
    ACTIVE("active", "进行中"),
    COMPLETED("completed", "已完成");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    EnrollmentStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}