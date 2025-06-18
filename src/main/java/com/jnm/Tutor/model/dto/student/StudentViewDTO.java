package com.jnm.Tutor.model.dto.student;

import com.jnm.Tutor.model.dto.StudentScoreDTO;
import com.jnm.Tutor.model.enums.CommonStatusEnum;
import com.jnm.Tutor.model.enums.EducationSystemEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentViewDTO {
    private String id;
    private String avatar;
    private String name;
    private Integer age;
    private Integer grade;
    private String phone;
    private String address;
    private String account;
    private EducationSystemEnum educationSystem;
    private CommonStatusEnum status;
    private LocalDateTime createdAt;
    private List<StudentScoreDTO> scores; // 包含学生的成绩列表
}