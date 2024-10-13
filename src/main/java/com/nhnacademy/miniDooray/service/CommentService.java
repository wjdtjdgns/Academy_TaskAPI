package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto registerComment(Long projectId,Long taskId, CommentDto commentDto);
    CommentDto getComment(Long projectId,Long taskId, Long id);
    CommentDto updateComment(Long projectId,Long taskId, Long id, CommentDto commentDto);
    void deleteComment(Long projectId,Long taskId, Long id);
    List<CommentDto> getAllComments(Long projectId, Long taskId);
}