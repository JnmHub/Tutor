package com.jnm.Tutor.controller;

import com.jnm.Tutor.model.Schedules;
import com.jnm.Tutor.model.dto.schedule.ScheduleCreateDTO;
import com.jnm.Tutor.service.SchedulesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "排课管理 (教师端)")
@RestController
@RequestMapping("/schedules")
@Secured("ROLE_TEACHER")
public class SchedulesController {

    @Autowired
    private SchedulesService schedulesService;

    @Operation(summary = "为课程添加新的可授课时间")
    @PostMapping
    public void create(@Valid @RequestBody ScheduleCreateDTO scheduleCreateDTO) {
        schedulesService.createSchedule(scheduleCreateDTO);
    }

    @Operation(summary = "查询指定课程的所有排课")
    @GetMapping("/course/{courseId}")
    public List<Schedules> getSchedulesByCourse(@PathVariable String courseId) {
        return schedulesService.getSchedulesByCourseForOwner(courseId);
    }

    @Operation(summary = "删除一个未被预约的排课")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        schedulesService.deleteSchedule(id);
    }
}