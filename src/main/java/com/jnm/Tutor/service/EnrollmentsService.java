package com.jnm.Tutor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jnm.Tutor.model.Enrollments;

public interface EnrollmentsService extends IService<Enrollments> {

    /**
     * 学生报名一个排课
     *
     * @param scheduleId 要报名的排课ID
     */
    void createEnrollment(String scheduleId);
}