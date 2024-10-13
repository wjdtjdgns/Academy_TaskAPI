package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.entity.TaskTag;
import com.nhnacademy.miniDooray.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/projects/{projectId}/tasks")
@RequiredArgsConstructor
@RestController
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "프로젝트 내 태스크 추가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "태스크가 성공적으로 등록되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (예: 유효하지 않은 프로젝트 ID)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트 아이디입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    @PostMapping
    public ResponseEntity<TaskDto> registerTask(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @Validated @RequestBody TaskRegisterDto taskDto) {
        TaskDto createdTask = taskService.registerTask(userId, projectId, taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @Operation(summary = "프로젝트 내 태스크 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDto> updateTask(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long taskId, @Validated @RequestBody TaskDto taskDto) {
        TaskDto updatedTask = taskService.updateTask(taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "프로젝트 내 태스크 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task successfully deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "프로젝트 내 태스크 전체 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Tasks retrieved"),
//            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "Forbidden"),
//            @ApiResponse(responseCode = "404", description = "Project not found")
//    })
//    @GetMapping
//    public ResponseEntity<Page<TaskDto>> getTasks(
//            @RequestHeader("X-USER-ID") String userId,
//            Pageable pageable) {
//        Page<TaskDto> taskDtoList = taskService.getTasks(pageable.getPageNumber(), pageable.getPageSize());
//        return ResponseEntity.ok(taskDtoList);
//    }

    @Operation(summary = "프로젝트 내 태스크 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasks retrieved"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasksByProjectId(@PathVariable Long projectId) {
        List<TaskDto> taskDtoList = taskService.getTasksByProjectId(projectId);

        return ResponseEntity.ok().body(taskDtoList);
    }

    @Operation(summary = "프로젝트 내 태스크 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTask(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long taskId) {
        TaskDto taskDto = taskService.getTask(taskId);
        return ResponseEntity.ok(taskDto);
    }

    @Operation(summary = "Update a milestone to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Milestone successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{taskId}/milestone")
    public ResponseEntity<TaskDto> updateMilestone(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long taskId,
            @RequestParam Long milestoneId,
            @Validated @RequestBody MilestoneDto milestoneDto) {
        TaskDto updatedTask = taskService.updateMilestone(taskId, milestoneId, milestoneDto);
        return ResponseEntity.ok(updatedTask);
    }

    @Operation(summary = "Update a tag to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{taskTagId}")
    public ResponseEntity<TaskTagDto> updateTag(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long taskTagId,
            @Validated @RequestBody TaskDto taskDto,
            @Validated @RequestBody TagDto tagDto) {
        TaskTagDto updatedTaskTag = taskService.updateTag(taskTagId, taskDto, tagDto);
        return ResponseEntity.ok(updatedTaskTag);
    }

    @Operation(summary = "태스크에 태그 추가", description = "프로젝트 내 특정 태스크에 태그를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "태그가 성공적으로 추가되었습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. (예: 유효하지 않은 태그 ID)"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 프로젝트 또는 태스크 아이디입니다."),
            @ApiResponse(responseCode = "500", description = "서버 오류입니다.")
    })
    @PostMapping("/{taskId}")
    public ResponseEntity<List<TaskTagResponse>> registerTag(@RequestHeader("X-USER-ID") String userId,
                                                             @PathVariable Long projectId,
                                                             @PathVariable Long taskId,
                                                             @RequestBody TaskTagRequest taskTagRequest) {
        List<TaskTagResponse> responseList = taskService.registerTags(userId, projectId, taskId, taskTagRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseList);
    }

    @GetMapping("/{taskId}/tag")
    public ResponseEntity<List<TaskTag>> getTagsByTaskId(@RequestHeader("X-USER-ID") String userId,
                                                         @PathVariable Long projectId,
                                                         @PathVariable Long taskId
    ) {
        List<TaskTag> responseList = taskService.getTagByTaskId(taskId);

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }
}