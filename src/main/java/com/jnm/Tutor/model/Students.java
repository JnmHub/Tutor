package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生信息表实体类
 */
@Data
@TableName("jnm_students")
public class Students {

    /**
     * 学生ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

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
     * 年级 (4-9)
     */
    @TableField("grade")
    private Integer grade;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

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
     * 学制 (6-3制, 5-4制)
     */
    @TableField("education_system")
    private String educationSystem;

    /**
     * 状态 (正常, 禁用)
     */
    @TableField("status")
    private String status;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}