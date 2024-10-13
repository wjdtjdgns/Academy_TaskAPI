package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    private TaskDto taskDto;
    private Project project;
    private Milestone milestone;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project(1L, "Test Project", "admin1", Status.ACTIVATED);
        milestone = new Milestone(1L, project, "milestone Title", ZonedDateTime.now(), ZonedDateTime.now().plusDays(7));
        taskDto = new TaskDto(1L, milestone, project, "Test Task", "This is a test task.");
    }

    @Test
    void registerTask_ShouldReturnCreatedTask() {
        when(taskService.registerTask(any(TaskDto.class))).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.registerTask("userId", taskDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        when(taskService.updateTask(eq(1L), any(TaskDto.class))).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.updateTask("userId", 1L, taskDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
    }

    @Test
    void deleteTask_ShouldReturnNoContent() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<Void> response = taskController.deleteTask("userId", 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1L);
    }

//    @Test
//    void getTasks_ShouldReturnTaskList() {
//        Page<TaskDto> taskPage = new PageImpl<>(Collections.singletonList(taskDto));
//        when(taskService.getTasks(anyInt(), anyInt())).thenReturn(taskPage);
//
//        ResponseEntity<Page<TaskDto>> response = taskController.getTasks("userId", Pageable.ofSize(10));
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(taskPage, response.getBody());
//    }

    @Test
    void getTask_ShouldReturnTask() {
        when(taskService.getTask(1L)).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.getTask("userId", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
    }

    @Test
    void updateMilestone_ShouldReturnUpdatedTask() {
        MilestoneDto milestoneDto = new MilestoneDto(
                milestone.getId(),
                milestone.getProject(),
                milestone.getTitle(),
                milestone.getStartDate(),
                milestone.getEndDate()
        );
        when(taskService.updateMilestone(eq(1L), eq(1L), any(MilestoneDto.class))).thenReturn(taskDto);

        ResponseEntity<TaskDto> response = taskController.updateMilestone("userId", 1L, 1L, milestoneDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());
    }

    @Test
    void updateTag_ShouldReturnUpdatedTaskTag() {
        TaskTagDto taskTagDto = new TaskTagDto();
        when(taskService.updateTag(eq(1L), any(TaskDto.class), any())).thenReturn(taskTagDto);

        TagDto tagDto = new TagDto(1L, project, "Test Tag");
        ResponseEntity<TaskTagDto> response = taskController.updateTag("userId", 1L, taskDto, tagDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskTagDto, response.getBody());
    }
}