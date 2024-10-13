package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.dto.MilestoneRegisterDto;

import java.util.List;

public interface MilestoneService {

    MilestoneDto addMilestoneToProject(String userId, Long projectId, MilestoneRegisterDto milestoneDto);
    List<MilestoneDto> getMilestonesByProjectId(String userId, Long projectId);
    MilestoneDto updateMilestone(String userId, Long projectId, MilestoneDto milestoneDto);
    void deleteProjectMilestone(String userId, Long projectId, Long milestoneId);
}
