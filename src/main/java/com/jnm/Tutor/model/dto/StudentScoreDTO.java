package com.jnm.Tutor.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentScoreDTO {

    @NotBlank(message = "科目名称不能为空")
    private String subjectName;

    @NotNull(message = "科目分数不能为空")
    @DecimalMin(value = "0.0", message = "分数不能为负数")
    private BigDecimal score;
}