package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.mapper.EnrollmentsMapper;
import com.jnm.Tutor.model.Courses;
import com.jnm.Tutor.model.Enrollments;
import com.jnm.Tutor.model.Orders;
import com.jnm.Tutor.model.Schedules;
import com.jnm.Tutor.model.enums.EnrollmentStatusEnum;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.model.enums.OrderStatusEnum;
import com.jnm.Tutor.model.enums.ScheduleStatusEnum;
import com.jnm.Tutor.security.utils.SecurityUtils;
import com.jnm.Tutor.service.CoursesService;
import com.jnm.Tutor.service.EnrollmentsService;
import com.jnm.Tutor.service.OrdersService;
import com.jnm.Tutor.service.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class EnrollmentsServiceImpl extends ServiceImpl<EnrollmentsMapper, Enrollments> implements EnrollmentsService {

    @Autowired
    private SchedulesService schedulesService;

    @Autowired
    private CoursesService coursesService;

    @Autowired
    private OrdersService ordersService;

    @Override
    @Transactional // *** 关键：声明此方法为事务性操作 ***
    public void createEnrollment(String scheduleId) {
        // 1. 获取当前登录的学生ID (权限安全)
        String studentId = SecurityUtils.getCurrentUser().getId();

        // 2. 锁定并校验排课信息 (数据校验与安全)
        Schedules schedule = schedulesService.getById(scheduleId);
        if (schedule == null) {
            throw new ValidatedException(ErrorEnum.NOT_EXIST_ERROR.getCode(), "该排课不存在");
        }
        // 必须是“可选”状态才能报名
        if (!Objects.equals(schedule.getStatus(), ScheduleStatusEnum.AVAILABLE.getCode())) {
            throw new ValidatedException(-1, "手慢了，该时间段已被预约或已结束");
        }

        // 3. 获取课程信息，用于快照
        Courses course = coursesService.getById(schedule.getCourseId());
        if (course == null || course.getIsDeleted()) {
            throw new ValidatedException(ErrorEnum.NOT_EXIST_ERROR.getCode(), "关联的课程不存在或已下架");
        }

        // 4. 合理性校验：教师不能报名自己的课程
        if(Objects.equals(course.getTeacherId(), studentId)){
            throw new ValidatedException(-1, "您不能报名自己发布的课程");
        }

        // --- 开始执行数据库写操作 ---

        // 5. 更新排课状态为“已预约”
        schedule.setStatus(ScheduleStatusEnum.BOOKED.getCode());
        schedulesService.updateById(schedule);

        // 6. 创建报名记录 (数据快照)
        Enrollments enrollment = new Enrollments();
        enrollment.setStudentId(studentId);
        enrollment.setTeacherId(schedule.getTeacherId());
        enrollment.setCourseId(schedule.getCourseId());
        enrollment.setScheduleId(schedule.getId());
        // 拷贝课程信息，保证历史订单的准确性
        enrollment.setCopiedCourseTitle(course.getTitle());
        enrollment.setCopiedSubject(course.getSubject());
        enrollment.setCopiedPrice(course.getPrice());
        // 设置初始状态为“进行中”
        enrollment.setStatus(EnrollmentStatusEnum.ACTIVE.getCode());
        enrollment.setCreatedAt(LocalDateTime.now());
        this.save(enrollment); // 保存后，enrollment对象会获得ID

        // 7. 创建关联订单
        Orders order = new Orders();
        order.setEnrollmentId(enrollment.getId()); // 关联刚刚创建的报名记录
        order.setStudentId(studentId);
        order.setAmount(course.getPrice());
        // 设置初始状态为“待确认”
        order.setStatus(OrderStatusEnum.PENDING_CONFIRMATION.getCode());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        ordersService.save(order);
    }
}