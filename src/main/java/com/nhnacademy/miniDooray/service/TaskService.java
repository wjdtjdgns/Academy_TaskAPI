package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.entity.TaskTag;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    TaskDto registerTask(String userId, Long projectId, TaskRegisterDto taskDto);
    TaskDto updateTask(Long taskId, TaskDto taskDto);
    void deleteTask(Long taskId);
    Page<TaskDto> getTasks(int page, int size);
    TaskDto getTask(Long taskId);
    TaskDto updateMilestone(Long taskId, Long milestoneId, MilestoneDto milestoneDto);
    TaskTagDto updateTag(Long taskTagId, TaskDto taskDto, TagDto tagDto);
    List<TaskDto> getTasksByProjectId(Long projectId);
    List<TaskTagResponse> registerTags(String userId, Long projectId, Long taskId, TaskTagRequest taskTagRequest);
    List<TaskTag> getTagByTaskId(Long taskId);
}