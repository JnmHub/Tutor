package com.jnm.Tutor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CommonStatusEnum {
    ACTIVE("active", "正常"),
    BANNED("banned", "禁用");

    @EnumValue // 标记数据库存的值是code
    private final String code;

    @JsonValue // 标记json返回的是desc
    private final String desc;

    CommonStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}