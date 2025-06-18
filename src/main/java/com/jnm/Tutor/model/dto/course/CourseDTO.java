package com.jnm.Tutor.model.dto.course;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDTO {
    @NotBlank(message = "课程标题不能为空")
    private String title;

    @NotBlank(message = "课程介绍不能为空")
    private String description;

    @NotBlank(message = "科目不能为空")
    private String subject;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "价格必须大于0")
    private BigDecimal price;
}