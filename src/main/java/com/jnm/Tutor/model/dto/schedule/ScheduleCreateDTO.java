package com.jnm.Tutor.model.dto.schedule;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleCreateDTO {
    @NotBlank(message = "必须关联一个课程ID")
    private String courseId;

    @NotNull(message = "上课日期不能为空")
    @FutureOrPresent(message = "上课日期不能是过去的时间")
    private LocalDate courseDate;

    @NotNull(message = "开始时间不能为空")
    private LocalTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalTime endTime;
}