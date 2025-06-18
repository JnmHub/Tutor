package com.jnm.Tutor.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表实体类
 */
@Data
@TableName("jnm_orders")
public class Orders {

    /**
     * 订单ID (UUID)
     */
    @TableId
    private String id;

    /**
     * 关联的报课记录ID
     */
    @TableField("enrollment_id")
    private String enrollmentId;

    /**
     * 下单学生ID
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 订单金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 订单状态 (待确认, 已确认, 已取消)
     */
    @TableField("status")
    private String status;

    /**
     * 订单创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 订单更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
