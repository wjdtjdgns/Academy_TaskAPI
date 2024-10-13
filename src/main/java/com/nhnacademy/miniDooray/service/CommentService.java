package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.dto.CommentRegisterDto;

import java.util.List;

public interface CommentService {
    CommentDto registerComment(String userId, Long projectId,Long taskId, CommentRegisterDto commentDto);
    CommentDto getComment(Long projectId,Long taskId, Long id);
    CommentDto updateComment(Long projectId,Long taskId, Long id, CommentDto commentDto);
    void deleteComment(Long projectId,Long taskId, Long id);
    List<CommentDto> getAllComments(Long projectId, Long taskId);
}