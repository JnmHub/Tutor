package com.jnm.Tutor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
public enum OrderStatusEnum {
    PENDING_CONFIRMATION("pending_confirmation", "待确认"),
    CONFIRMED("confirmed", "已确认"),
    CANCELLED("cancelled", "已取消");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    OrderStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}