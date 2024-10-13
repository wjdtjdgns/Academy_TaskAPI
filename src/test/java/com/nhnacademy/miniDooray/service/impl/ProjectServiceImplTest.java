package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectRegisterDto;
import com.nhnacademy.miniDooray.dto.ProjectUpdateDto;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.ProjectMember;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.exception.ProjectNameAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.ProjectNotFoundException;
import com.nhnacademy.miniDooray.repository.ProjectMemberRepository;
import com.nhnacademy.miniDooray.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Test
    void getProjectsByMemberId_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> projectPage = new PageImpl<>(Collections.singletonList(new Project()));

        when(projectRepository.findProjectsByMemberId(anyString(), any())).thenReturn(projectPage);

        Page<ProjectDto> result = projectService.getProjectsByMemberId("member1", pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void createProject_success() {
        ProjectRegisterDto projectDto = new ProjectRegisterDto("New Project");

        when(projectRepository.existsByName("New Project")).thenReturn(false);

        Project mockProject = new Project();
        mockProject.setId(1L);
        mockProject.setAdminId("admin1");
        mockProject.setName("New Project");
        mockProject.setStatus(Status.ACTIVATED);

        when(projectRepository.save(any(Project.class))).thenReturn(mockProject);
        when(projectMemberRepository.save(any())).thenReturn(new ProjectMember());

        // 실제 createProject 호출
        ProjectDto result = projectService.createProject("admin1", projectDto);

        // 결과 확인
        assertNotNull(result);
        assertEquals("New Project", result.getName());
        assertEquals("admin1", result.getAdminId());

        verify(projectMemberRepository).save(any(ProjectMember.class));
    }

    @Test
    void createProject_projectNameExists() {
        ProjectRegisterDto projectDto = new ProjectRegisterDto("Existing Project");
        when(projectRepository.existsByName("Existing Project")).thenReturn(true);

        assertThrows(ProjectNameAlreadyExistsException.class, () -> projectService.createProject("admin1", projectDto));
    }


    @Test
    void updateProject_success() {
        Project project = new Project();
        project.setId(1L);

        ProjectUpdateDto projectUpdateDto = new ProjectUpdateDto("Updated Project", Status.CLOSED);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectDto result = projectService.updateProject("user1", 1L, projectUpdateDto);

        assertEquals("Updated Project", result.getName());
        assertEquals(Status.CLOSED, result.getStatus());
    }


    @Test
    void updateProject_projectNotFound() {
        ProjectUpdateDto projectUpdateDto = new ProjectUpdateDto("Non-existing Project", Status.CLOSED);

        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.updateProject("user1", 1L, projectUpdateDto));
    }

    @Test
    void getProjectsByMemberId_nullParameters() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectsByMemberId(null, pageable));
        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectsByMemberId("member1", null));
    }

    @Test
    void createProject_nullParameters() {
        ProjectRegisterDto projectDto = new ProjectRegisterDto("New Project");

        assertThrows(IllegalArgumentException.class, () -> projectService.createProject(null, projectDto));
        assertThrows(IllegalArgumentException.class, () -> projectService.createProject("admin1", null));
    }

    @Test
    void updateProject_nullParameters() {
        ProjectUpdateDto projectUpdateDto = new ProjectUpdateDto("Updated Project", Status.CLOSED);

        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(null, 1L, projectUpdateDto));
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject("user1", null, projectUpdateDto));
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject("user1", 1L, null));
    }


    @Test
    void addMembersToProject_nullParameters() {
        List<String> memberIds = Collections.singletonList("member1");

        assertThrows(IllegalArgumentException.class, () -> projectService.addMembersToProject(null, 1L, memberIds));
        assertThrows(IllegalArgumentException.class, () -> projectService.addMembersToProject("admin1", null, memberIds));
        assertThrows(IllegalArgumentException.class, () -> projectService.addMembersToProject("admin1", 1L, null));
    }

    @Test
    void getProjectById_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectById(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectById("user1", null));
    }

    @Test
    void addMembersToProject_success() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));

        List<String> memberIds = Collections.singletonList("member1");

        ProjectDto result = projectService.addMembersToProject("admin1", 1L, memberIds);

        assertNotNull(result);
        verify(projectMemberRepository, times(1)).save(any(ProjectMember.class));
    }

    @Test
    void addMembersToProject_projectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        List<String> memberIds = Collections.singletonList("member1");

        assertThrows(ProjectNotFoundException.class, () -> projectService.addMembersToProject("admin1", 1L, memberIds));
    }

    @Test
    void getProjectById_projectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> projectService.getProjectById("user1", 1L));
    }

    @Test
    void getProjectById_success() {
        Project project = new Project();
        project.setId(1L);
        project.setAdminId("admin1");
        project.setName("Test Project");

        List<ProjectMember> projectMembers = Collections.singletonList(new ProjectMember(1L, "member1", project));

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMemberRepository.findByProjectId(1L)).thenReturn(projectMembers);

        ProjectDto result = projectService.getProjectById("user1", 1L);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        assertEquals("admin1", result.getAdminId());
        assertEquals(1, result.getMemberIds().size());
        assertEquals("member1", result.getMemberIds().get(0));

        verify(projectRepository, times(1)).findById(1L);
        verify(projectMemberRepository, times(1)).findByProjectId(1L);
    }
}