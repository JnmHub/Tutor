package com.jnm.Tutor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnm.Tutor.exception.ValidatedException;
import com.jnm.Tutor.mapper.SchedulesMapper;
import com.jnm.Tutor.model.Courses;
import com.jnm.Tutor.model.Schedules;
import com.jnm.Tutor.model.dto.schedule.ScheduleCreateDTO;
import com.jnm.Tutor.model.enums.ErrorEnum;
import com.jnm.Tutor.model.enums.ScheduleStatusEnum;
import com.jnm.Tutor.security.utils.SecurityUtils;
import com.jnm.Tutor.service.CoursesService;
import com.jnm.Tutor.service.SchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class SchedulesServiceImpl extends ServiceImpl<SchedulesMapper, Schedules> implements SchedulesService {

    @Autowired
    private CoursesService coursesService;

    @Override
    public void createSchedule(ScheduleCreateDTO dto) {
        String teacherId = SecurityUtils.getCurrentUser().getId();

        // 1. 校验课程是否存在且属于当前教师
        Courses course = coursesService.getById(dto.getCourseId());
        if (course == null || !Objects.equals(course.getTeacherId(), teacherId)) {
            throw new ValidatedException(ErrorEnum.NO_AUTHORITY.getCode(), "课程不存在或您无权操作");
        }

        // 2. 校验结束时间必须晚于开始时间
        if (dto.getEndTime().isBefore(dto.getStartTime()) || dto.getEndTime().equals(dto.getStartTime())) {
            throw new ValidatedException(-1, "结束时间必须晚于开始时间");
        }

        // 3. 校验时间冲突
        QueryWrapper<Schedules> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId)
                .eq("course_date", dto.getCourseDate())
                // (新开始 < 旧结束) AND (新结束 > 旧开始)
                .lt("start_time", dto.getEndTime())
                .gt("end_time", dto.getStartTime());
        if (this.count(queryWrapper) > 0) {
            throw new ValidatedException(-1, "该时间段与您已有的排课存在冲突");
        }

        // 4. 创建并保存排课信息
        Schedules schedule = new Schedules();
        schedule.setCourseId(dto.getCourseId());
        schedule.setTeacherId(teacherId);
        schedule.setCourseDate(dto.getCourseDate());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setStatus(ScheduleStatusEnum.AVAILABLE.getCode());
        this.save(schedule);
    }

    @Override
    public void deleteSchedule(String scheduleId) {
        Schedules schedule = this.getById(scheduleId);
        if (schedule == null) {
            throw new ValidatedException(ErrorEnum.NOT_EXIST_ERROR);
        }

        // 校验权限
        String currentTeacherId = SecurityUtils.getCurrentUser().getId();
        if (!Objects.equals(schedule.getTeacherId(), currentTeacherId)) {
            throw new ValidatedException(ErrorEnum.NO_AUTHORITY);
        }
        // 只有“可选”状态的排课才能被删除
        if (!Objects.equals(schedule.getStatus(), ScheduleStatusEnum.AVAILABLE.getCode())) {
            throw new ValidatedException(-1, "该排课已被预约或已完成，无法删除");
        }

        this.removeById(scheduleId);
    }
    @Override
    public List<Schedules> getSchedulesByCourseForOwner(String courseId) {
        String currentTeacherId = SecurityUtils.getCurrentUser().getId();

        // 1. 先验证课程是否存在，以及是否属于当前登录的教师
        Courses course = coursesService.getById(courseId);
        if (course == null || !Objects.equals(course.getTeacherId(), currentTeacherId)) {
            // 如果课程不存在，或课程不属于当前教师，则抛出无权限异常
            throw new ValidatedException(ErrorEnum.NO_AUTHORITY);
        }

        // 2. 验证通过后，才查询并返回该课程的排课信息
        return this.list(new QueryWrapper<Schedules>().eq("course_id", courseId));
    }
}