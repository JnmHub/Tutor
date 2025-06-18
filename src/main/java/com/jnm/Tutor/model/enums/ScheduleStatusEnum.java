package com.jnm.Tutor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 课程排期状态枚举
 */
@Getter
public enum ScheduleStatusEnum {
    AVAILABLE("available", "可选"),
    BOOKED("booked", "已预约"),
    COMPLETED("completed", "已完成");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    ScheduleStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}