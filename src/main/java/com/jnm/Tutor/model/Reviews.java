package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
/**
 * 教师评价表实体类
 */
@Data
@TableName("jnm_reviews")
public class Reviews {

    /**
     * 评价ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 关联的报课记录ID
     */
    @TableField("enrollment_id")
    private String enrollmentId;

    /**
     * 评价的学生ID
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 被评价的教师ID
     */
    @TableField("teacher_id")
    private String teacherId;

    /**
     * 教学态度分数 (1-5)
     */
    @TableField("teaching_attitude_score")
    private Integer teachingAttitudeScore;

    /**
     * 服务态度分数 (1-5)
     */
    @TableField("service_attitude_score")
    private Integer serviceAttitudeScore;

    /**
     * 文字评论内容
     */
    @TableField("comment")
    private String comment;

    /**
     * 评价时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}