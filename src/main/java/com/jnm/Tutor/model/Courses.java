package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程信息表实体类
 */
@Data
@TableName("jnm_courses")
public class Courses {

    /**
     * 课程ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 发布课程的教师ID
     */
    @TableField("teacher_id")
    private String teacherId;

    /**
     * 课程标题
     */
    @TableField("title")
    private String title;

    /**
     * 课程介绍
     */
    @TableField("description")
    private String description;

    /**
     * 科目
     */
    @TableField("subject")
    private String subject;

    /**
     * 价格 (每课时)
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 是否软删除 (true为删除)
     */
    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}