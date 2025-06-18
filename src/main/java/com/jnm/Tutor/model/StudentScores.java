package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 学生成绩表实体类
 */
@Data
@TableName("jnm_student_scores")
public class StudentScores {

    /**
     * 成绩ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 关联的学生ID
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 科目名称
     */
    @TableField("subject_name")
    private String subjectName;

    /**
     * 科目分数
     */
    @TableField("score")
    private BigDecimal score;
}