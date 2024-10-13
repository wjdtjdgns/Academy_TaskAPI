package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectRegisterDto;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.ProjectMember;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.exception.ProjectNameAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.ProjectNotFoundException;
import com.nhnacademy.miniDooray.repository.ProjectMemberRepository;
import com.nhnacademy.miniDooray.repository.ProjectRepository;
import com.nhnacademy.miniDooray.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public Page<ProjectDto> getProjectsByMemberId(String memberId, Pageable pageable) {
        if (memberId == null || pageable == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Page<Project> projectPage = projectRepository.findProjectsByMemberId(memberId, pageable);
        return projectPage.map(this::convertToDto);
    }

    private ProjectDto convertToDto(Project project) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
        List<String> memberIds = members.stream()
                .map(ProjectMember::getMemberId)
                .collect(Collectors.toList());

        return new ProjectDto(
                project.getId(),
                project.getAdminId(),
                project.getName(),
                project.getStatus(),
                memberIds
        );
    }

    @Override
    public ProjectDto createProject(String adminId, ProjectRegisterDto projectDto) {
        if (adminId == null || projectDto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        if (projectRepository.existsByName(projectDto.getName())) {
            throw new ProjectNameAlreadyExistsException("Project with the same name already exists");
        }

        Project project = new Project();
        project.setAdminId(adminId);
        project.setName(projectDto.getName());
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);
        return convertToDto(project);
    }

    @Override
    public ProjectDto updateProject(String userId, Long projectId, ProjectDto projectDto) {
        if (userId == null || projectId == null || projectDto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        project.setName(projectDto.getName());
        project.setStatus(projectDto.getStatus());
        projectRepository.save(project);
        return convertToDto(project);
    }

    @Override
    public ProjectDto addMembersToProject(String adminId, Long projectId, List<String> memberIds) {
        if (adminId == null || projectId == null || memberIds == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        for (String memberId : memberIds) {
            ProjectMember member = new ProjectMember();
            member.setMemberId(memberId);
            member.setProject(project);
            projectMemberRepository.save(member);
        }

        return convertToDto(project);
    }

    @Override
    public ProjectDto getProjectById(String userId, Long projectId) {
        if (userId == null || projectId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
        return convertToDto(project);
    }
}

