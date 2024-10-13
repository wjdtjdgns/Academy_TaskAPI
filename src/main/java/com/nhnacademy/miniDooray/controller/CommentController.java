package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.dto.CommentRegisterDto;
import com.nhnacademy.miniDooray.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Register a new comment", description = "로그인 후 사용 가능. 댓글을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글이 성공적으로 생성됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (예: 필수 필드 누락)"),
            @ApiResponse(responseCode = "404", description = "해당 프로젝트 또는 Task ID를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<CommentDto> registerComment(@RequestHeader("X-USER-ID") String userId,
                                                      @PathVariable Long projectId,
                                                      @PathVariable Long taskId,
                                                      @Validated @RequestBody CommentRegisterDto commentDto) {
        CommentDto registerDto = commentService.registerComment(userId, projectId, taskId, commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDto);
    }

    @Operation(summary = "Get a comment by ID", description = "특정 댓글의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정할 댓글 정보를 성공적으로 반환함"),
            @ApiResponse(responseCode = "404", description = "해당 프로젝트, Task 또는 Comment ID를 찾을 수 없음")
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long commentId){
        CommentDto commentDto = commentService.getComment(projectId, taskId, commentId);
        return ResponseEntity.ok(commentDto);
    }

    @Operation(summary = "Update a comment", description = "특정 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글이 성공적으로 업데이트됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "해당 프로젝트, Task 또는 Comment ID를 찾을 수 없음")
    })
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long commentId, @Validated @RequestBody CommentDto commentDto){
        CommentDto updatedComment = commentService.updateComment(projectId, taskId, commentId, commentDto);
        return ResponseEntity.ok(updatedComment);

    }

    @Operation(summary = "Delete a comment", description = "특정 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글이 성공적으로 삭제됨 (본문 없음)"),
            @ApiResponse(responseCode = "404", description = "해당 프로젝트, Task 또는 Comment ID를 찾을 수 없음")
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long commentId){
        commentService.deleteComment(projectId, taskId, commentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all comments", description = "Task에 대한 모든 댓글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 댓글을 성공적으로 반환함"),
            @ApiResponse(responseCode = "404", description = "해당 프로젝트 또는 Task ID를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable Long projectId, @PathVariable Long taskId){
        List<CommentDto> comments = commentService.getAllComments(projectId, taskId);
        return ResponseEntity.ok(comments);
    }
}