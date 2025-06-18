package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 学生报课记录表实体类
 */
@Data
@TableName("jnm_enrollments")
public class Enrollments {

    /**
     * 报名ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 学生ID
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 教师ID
     */
    @TableField("teacher_id")
    private String teacherId;

    /**
     * 原始课程ID
     */
    @TableField("course_id")
    private String courseId;

    /**
     * 所选的具体排课ID
     */
    @TableField("schedule_id")
    private String scheduleId;

    /**
     * 报名时拷贝的课程标题
     */
    @TableField("copied_course_title")
    private String copiedCourseTitle;

    /**
     * 报名时拷贝的科目
     */
    @TableField("copied_subject")
    private String copiedSubject;

    /**
     * 报名时拷贝的价格
     */
    @TableField("copied_price")
    private BigDecimal copiedPrice;

    /**
     * 学习状态 (进行中, 已完成)
     */
    @TableField("status")
    private String status;

    /**
     * 报名时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}