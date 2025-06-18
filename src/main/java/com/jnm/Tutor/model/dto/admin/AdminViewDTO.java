package com.jnm.Tutor.model.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminViewDTO {
    private String id;
    private String name;
    private String account;
    private LocalDateTime createdAt;
}