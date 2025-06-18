package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 教师信息表实体类
 */
@Data
@TableName("jnm_teachers")
public class Teachers {

    /**
     * 教师ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 身份证照片URL
     */
    @TableField("id_card_photo")
    private String idCardPhoto;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 登录账号
     */
    @TableField("account")
    private String account;

    /**
     * 登录密码 (加密存储)
     */
    @TableField("password")
    private String password;

    /**
     * 状态 (待审核, 正常, 禁用)
     */
    @TableField("status")
    private String status;

    /**
     * 擅长科目 (逗号分隔)
     */
    @TableField("teaching_subjects")
    private String teachingSubjects;

    /**
     * 教学经验描述
     */
    @TableField("teaching_experience")
    private String teachingExperience;

    /**
     * 毕业院校
     */
    @TableField("university")
    private String university;

    /**
     * 教学态度平均分
     */
    @TableField("avg_teaching_attitude_score")
    private BigDecimal avgTeachingAttitudeScore;

    /**
     * 服务态度平均分
     */
    @TableField("avg_service_attitude_score")
    private BigDecimal avgServiceAttitudeScore;



    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}
