package com.jnm.Tutor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jnm.Tutor.model.Schedules;
import com.jnm.Tutor.model.dto.schedule.ScheduleCreateDTO;
import java.util.List;

/**
 * 排课服务接口
 * @author jnm
 */
public interface SchedulesService extends IService<Schedules> {
    // 创建排课并校验时间冲突
    void createSchedule(ScheduleCreateDTO scheduleCreateDTO);
    // 删除排课并校验权限和状态
    void deleteSchedule(String scheduleId);

    /**
     * 获取指定课程的排课列表（校验所有权）
     * @param courseId 课程ID
     * @return 排课列表
     */
    List<Schedules> getSchedulesByCourseForOwner(String courseId);
}