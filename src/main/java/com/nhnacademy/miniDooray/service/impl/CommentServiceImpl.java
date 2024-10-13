package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.entity.Comment;
import com.nhnacademy.miniDooray.entity.Task;
import com.nhnacademy.miniDooray.exception.IdAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.IdNotFoundException;
import com.nhnacademy.miniDooray.repository.CommentRepository;
import com.nhnacademy.miniDooray.repository.TaskRepository;
import com.nhnacademy.miniDooray.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Override
    public CommentDto registerComment(Long projectId,Long taskId, CommentDto commentDto) {
        if(Objects.isNull(commentDto)){
            throw new IllegalStateException();
        }

        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new IdNotFoundException("업무 또는 프로젝트가 존재하지 않습니다."));

        Comment comment = new Comment(
                null,
                task,
                commentDto.getMemberId(),
                commentDto.getContent()
        );

        commentRepository.save(comment);
        return convertToDto(comment);
    }

    @Override
    public CommentDto getComment(Long projectId, Long taskId, Long id) {
        if(Objects.isNull(id)){
            throw new IllegalStateException();
        }

        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new IdNotFoundException("업무 또는 프로젝트가 존재하지 않습니다."));

        Comment comment = commentRepository.findByTaskAndId(task, id)
                .orElseThrow(() -> new IdNotFoundException("댓글을 찾을 수 없습니다."));


        return convertToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long projectId, Long taskId, Long id, CommentDto commentDto) {
        if(Objects.isNull(id)){
            throw new IllegalStateException();
        }

        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new IdNotFoundException("업무 또는 프로젝트가 존재하지 않습니다."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id가 없습니다.")); //exception 추가?

        if(Objects.nonNull(commentDto.getContent())){
            comment.setContent(commentDto.getContent());
        }

        commentRepository.save(comment);

        return convertToDto(comment);
    }

    @Override
    public void deleteComment(Long projectId, Long taskId, Long id) {
        if(Objects.isNull(id)){
            throw new IllegalStateException();
        }

        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new IdNotFoundException("업무 또는 프로젝트가 없습니다."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("id가 없습니다.")); // exception 추가?

        commentRepository.delete(comment);

    }

    @Override
    public List<CommentDto> getAllComments(Long projectId, Long taskId){
        Task task = taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new IdNotFoundException("업무 또는 프로젝트가 없습니다."));

        List<Comment> comments = commentRepository.findAllByTask(task);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDto convertToDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getTask(),
                comment.getMemberId(),
                comment.getContent()
        );
    }

}