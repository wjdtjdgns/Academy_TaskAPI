package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectRegisterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {

    Page<ProjectDto> getProjectsByMemberId(String memberId, Pageable pageable);
    ProjectDto createProject(String adminId, ProjectRegisterDto projectDto);
    ProjectDto updateProject(String userId, Long projectId, ProjectDto projectDto);
    ProjectDto addMembersToProject(String adminId, Long projectId, List<String> memberIds);
    ProjectDto getProjectById(String userId, Long projectId);
}