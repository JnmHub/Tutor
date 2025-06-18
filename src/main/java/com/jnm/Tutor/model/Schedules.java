package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 课程排期表实体类
 */
@Data
@TableName("jnm_schedules")
public class Schedules {

    /**
     * 排课ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 关联的课程ID
     */
    @TableField("course_id")
    private String courseId;

    /**
     * 关联的教师ID
     */
    @TableField("teacher_id")
    private String teacherId;

    /**
     * 上课日期
     */
    @TableField("course_date")
    private LocalDate courseDate;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalTime endTime;

    /**
     * 状态 (可选, 已预约, 已完成)
     */
    @TableField("status")
    private String status;
}