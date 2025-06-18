package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员信息表实体类
 */
@Data
@TableName("jnm_admins")
public class Admins {

    /**
     * 管理员ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

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
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
}