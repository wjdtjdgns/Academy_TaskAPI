package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.exception.MilestoneNotFoundException;
import com.nhnacademy.miniDooray.exception.MilestoneTitleAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.ProjectNotFoundException;
import com.nhnacademy.miniDooray.repository.MilestoneRepository;
import com.nhnacademy.miniDooray.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MilestoneServiceImplTest {

    @InjectMocks
    private MilestoneServiceImpl milestoneService;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private ProjectRepository projectRepository;

    private Project project;
    private MilestoneDto milestoneDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project(1L, "admin1", "Project1", Status.ACTIVATED);
        milestoneDto = new MilestoneDto(1L, project, "Milestone 1", ZonedDateTime.now(), ZonedDateTime.now().plusDays(7));
    }

    /*@Test
    void addMilestoneToProject_success() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(milestoneRepository.existsByProjectAndTitle(any(Project.class), anyString())).thenReturn(false);
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MilestoneDto result = milestoneService.addMilestoneToProject("user1", 1L, milestoneDto);

        assertNotNull(result);
        assertEquals(milestoneDto.getTitle(), result.getTitle());
        verify(milestoneRepository, times(1)).save(any(Milestone.class));
    }*/

    /*@Test
    void addMilestoneToProject_projectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> milestoneService.addMilestoneToProject("user1", 1L, milestoneDto));
    }*/

    /*@Test
    void addMilestoneToProject_milestoneTitleAlreadyExists() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(milestoneRepository.existsByProjectAndTitle(any(Project.class), anyString())).thenReturn(true);

        assertThrows(MilestoneTitleAlreadyExistsException.class, () -> milestoneService.addMilestoneToProject("user1", 1L, milestoneDto));
    }*/

    @Test
    void getMilestonesByProjectId_success() {
        when(milestoneRepository.findByProjectId(anyLong())).thenReturn(Collections.singletonList(new Milestone()));

        List<MilestoneDto> result = milestoneService.getMilestonesByProjectId("user1", 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(milestoneRepository, times(1)).findByProjectId(anyLong());
    }

    @Test
    void updateMilestone_success() {
        Milestone milestone = new Milestone();
        milestone.setId(1L);
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.of(milestone));
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(milestone);

        MilestoneDto result = milestoneService.updateMilestone("user1", 1L, milestoneDto);

        assertEquals(milestoneDto.getTitle(), result.getTitle());
        verify(milestoneRepository, times(1)).save(any(Milestone.class));
    }

    @Test
    void updateMilestone_milestoneNotFound() {
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MilestoneNotFoundException.class, () -> milestoneService.updateMilestone("user1", 1L, milestoneDto));
    }

    @Test
    void deleteProjectMilestone_success() {
        when(milestoneRepository.existsById(anyLong())).thenReturn(true);

        milestoneService.deleteProjectMilestone("user1", 1L, 1L);

        verify(milestoneRepository, times(1)).deleteByProjectIdAndId(anyLong(), anyLong());
    }

    @Test
    void deleteProjectMilestone_milestoneNotFound() {
        when(milestoneRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(MilestoneNotFoundException.class, () -> milestoneService.deleteProjectMilestone("user1", 1L, 1L));
    }

    /*@Test
    void addMilestoneToProject_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> milestoneService.addMilestoneToProject(null, 1L, milestoneDto));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.addMilestoneToProject("user1", null, milestoneDto));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.addMilestoneToProject("user1", 1L, null));
    }*/

    @Test
    void getMilestonesByProjectId_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> milestoneService.getMilestonesByProjectId(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.getMilestonesByProjectId("user1", null));
    }

    @Test
    void updateMilestone_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> milestoneService.updateMilestone(null, 1L, milestoneDto));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.updateMilestone("user1", null, milestoneDto));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.updateMilestone("user1", 1L, null));
    }

    @Test
    void deleteProjectMilestone_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> milestoneService.deleteProjectMilestone(null, 1L, 1L));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.deleteProjectMilestone("user1", null, 1L));
        assertThrows(IllegalArgumentException.class, () -> milestoneService.deleteProjectMilestone("user1", 1L, null));
    }
}
