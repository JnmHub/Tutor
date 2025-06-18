package com.jnm.Tutor.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
/**
 * 学制枚举
 */
@Getter
public enum EducationSystemEnum {
    SIX_THREE("6-3", "6-3制"),
    FIVE_FOUR("5-4", "5-4制");

    @EnumValue
    private final String code;

    @JsonValue
    private final String desc;

    EducationSystemEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}