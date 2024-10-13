package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST - /projects/{projectId}/tasks/{taskId}/comments (댓글 등록 성공)")
    void testRegisterComment_success() throws Exception {
        CommentDto commentDto = new CommentDto(1L, null, "commentMemberId", "This is a test comment");

        Mockito.when(commentService.registerComment(anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(commentDto);

        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.content").value("This is a test comment"));
    }

    @Test
    @DisplayName("POST - /projects/{projectId}/tasks/{taskId}/comments 실패 - 잘못된 요청")
    void testRegisterComment_failure() throws Exception {
        CommentDto commentDto = new CommentDto(1L, null, "", "");

        mockMvc.perform(post("/projects/1/tasks/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET - /projects/{projectId}/tasks/{taskId}/comments/{commentId} (댓글 조회 성공)")
    void testGetComment_success() throws Exception {
        CommentDto commentDto = new CommentDto(1L, null, "commentMemberId", "This is a test comment");

        Mockito.when(commentService.getComment(anyLong(), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mockMvc.perform(get("/projects/1/tasks/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.content").value(commentDto.getContent()))
                .andExpect(jsonPath("$.memberId").value(commentDto.getMemberId()));
    }

    @Test
    @DisplayName("PUT - /projects/{projectId}/tasks/{taskId}/comments/{commentId} (댓글 수정 성공)")
    void testUpdateComment_success() throws Exception {
        CommentDto updatedCommentDto = new CommentDto(1L, null, "commentMemberId", "Updated comment");

        Mockito.when(commentService.updateComment(anyLong(), anyLong(), anyLong(), any(CommentDto.class)))
                .thenReturn(updatedCommentDto);

        mockMvc.perform(put("/projects/1/tasks/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCommentDto.getId()))
                .andExpect(jsonPath("$.content").value(updatedCommentDto.getContent()));
    }

    @Test
    @DisplayName("DELETE - /projects/{projectId}/tasks/{taskId}/comments/{commentId} (댓글 삭제 성공)")
    void testDeleteComment_success() throws Exception {
        Mockito.doNothing().when(commentService).deleteComment(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete("/projects/1/tasks/1/comments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET - /projects/{projectId}/tasks/{taskId}/comments (모든 댓글 조회 성공)")
    void testGetAllComments_success() throws Exception {
        CommentDto commentDto = new CommentDto(1L, null, "commentMemberId", "This is a test comment");

        Mockito.when(commentService.getAllComments(anyLong(), anyLong()))
                .thenReturn(List.of(commentDto));

        mockMvc.perform(get("/projects/1/tasks/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(commentDto.getId()))
                .andExpect(jsonPath("$[0].content").value(commentDto.getContent()));
    }
}
