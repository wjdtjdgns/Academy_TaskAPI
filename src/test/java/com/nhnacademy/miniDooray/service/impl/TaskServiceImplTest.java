package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.dto.TagDto;
import com.nhnacademy.miniDooray.dto.TaskDto;
import com.nhnacademy.miniDooray.dto.TaskTagDto;
import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Tag;
import com.nhnacademy.miniDooray.entity.Task;
import com.nhnacademy.miniDooray.entity.TaskTag;
import com.nhnacademy.miniDooray.exception.IdAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.IdNotFoundException;
import com.nhnacademy.miniDooray.repository.MilestoneRepository;
import com.nhnacademy.miniDooray.repository.TaskRepository;
import com.nhnacademy.miniDooray.repository.TaskTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private TaskTagRepository taskTagRepository;

    private Project project;
    private Milestone milestone;
    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        milestone = new Milestone();
        milestone.setId(1L);
        milestone.setTitle("Test Milestone");
        milestone.setProject(project);

        taskDto = new TaskDto(1L, milestone, project, "Test Task", "Test Content");

        task = new Task();
        task.setId(1L);
        task.setMilestone(milestone);
        task.setProject(project);
        task.setTitle("Test Task");
        task.setContent("Test Content");
    }

    @Test
    void updateTask_success() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDto result = taskService.updateTask(1L, taskDto);

        assertEquals(taskDto.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void updateTask_taskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.updateTask(1L, taskDto));
    }

    @Test
    void deleteTask_success() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    void deleteTask_taskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void getTasks_success() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(Arrays.asList(task), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        Page<TaskDto> result = taskService.getTasks(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    void getTask_success() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        TaskDto result = taskService.getTask(1L);

        assertNotNull(result);
        assertEquals(taskDto.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTask_taskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.getTask(1L));
    }

    @Test
    void updateMilestone_success() {
        MilestoneDto milestoneDto = new MilestoneDto(2L, project, "Updated Milestone", null, null);
        Milestone newMilestone = new Milestone();
        newMilestone.setId(2L);
        newMilestone.setTitle("Updated Milestone");

        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.of(newMilestone));

        TaskDto result = taskService.updateMilestone(1L, 2L, milestoneDto);

        assertEquals(milestoneDto.getTitle(), result.getMilestone().getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateMilestone_taskNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.updateMilestone(1L, 2L, new MilestoneDto()));
    }

    @Test
    void updateMilestone_milestoneNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(milestoneRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.updateMilestone(1L, 2L, new MilestoneDto()));
    }

    @Test
    void updateTag_success() {
        TagDto tagDto = new TagDto(1L, project, "Updated Tag");
        TaskTag taskTag = new TaskTag();
        taskTag.setId(1L);

        when(taskTagRepository.findById(anyLong())).thenReturn(Optional.of(taskTag));
        when(taskTagRepository.save(any(TaskTag.class))).thenReturn(taskTag);

        TaskTagDto result = taskService.updateTag(1L, taskDto, tagDto);

        assertEquals(tagDto.getName(), result.getTag().getName());
        verify(taskTagRepository, times(1)).save(taskTag);
    }

    @Test
    void updateTag_taskTagNotFound() {
        TagDto tagDto = new TagDto(1L, project, "Updated Tag");

        when(taskTagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> taskService.updateTag(1L, taskDto, tagDto));
    }

    @Test
    void updateTask_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(null, taskDto));
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTask(1L, null));
    }

    @Test
    void deleteTask_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> taskService.deleteTask(null));
    }

    @Test
    void getTask_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> taskService.getTask(null));
    }

    @Test
    void updateMilestone_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> taskService.updateMilestone(null, 2L, new MilestoneDto()));
        assertThrows(IllegalArgumentException.class, () -> taskService.updateMilestone(1L, null, new MilestoneDto()));
        assertThrows(IllegalArgumentException.class, () -> taskService.updateMilestone(1L, 2L, null));
    }

    @Test
    void updateTag_nullParameters() {
        TagDto tagDto = new TagDto(1L, project, "Updated Tag");
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTag(null, taskDto, tagDto));
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTag(1L, null, tagDto));
        assertThrows(IllegalArgumentException.class, () -> taskService.updateTag(1L, taskDto, null));
    }
}
