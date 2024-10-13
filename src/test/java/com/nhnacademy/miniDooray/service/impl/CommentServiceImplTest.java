package com.nhnacademy.miniDooray.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.entity.*;
import com.nhnacademy.miniDooray.repository.CommentRepository;
import com.nhnacademy.miniDooray.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    private Long projectId = 1L;
    private Long taskId = 1L;
    private Long commentId = 1L;
    private CommentDto commentDto;
    private Project project;
    private Milestone milestone;
    private Task task;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project(1L, "admin1", "Project1", Status.ACTIVATED);
        milestone = new Milestone(1L, project, "Milestone 1", ZonedDateTime.now(), ZonedDateTime.now().plusDays(7));
        task = new Task(1L, milestone, project, "taskTitle", "taskContent");
    }

    /*@Test
    void registerComment_ShouldSaveComment_WhenValidInput() {
        Task task = new Task(1L, milestone, project, "taskTitle", "taskContent");
        commentDto = new CommentDto(null, task, "member1", "This is a comment");

        when(taskRepository.findByProjectIdAndId(projectId, taskId)).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDto result = commentService.registerComment(projectId, taskId, commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getMemberId(), result.getMemberId());
        assertEquals(commentDto.getContent(), result.getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }*/


    @Test
    void getComment_ShouldReturnComment_WhenExists() {
        Task task = new Task();
        Comment comment = new Comment(commentId, task, "member1", "This is a comment");
        when(taskRepository.findByProjectIdAndId(projectId, taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findByTaskAndId(task, commentId)).thenReturn(Optional.of(comment));

        CommentDto result = commentService.getComment(projectId, taskId, commentId);

        assertNotNull(result);
        assertEquals(commentId, result.getId());
        assertEquals("member1", result.getMemberId());
    }

    @Test
    void updateComment_ShouldUpdateComment_WhenExists() {
        Task task = new Task(1L, milestone, project, "taskTitle", "taskContent");
        Comment existingComment = new Comment(commentId, task, "member1", "Old content");
        commentDto = new CommentDto(commentId, task, "member1", "Updated content");

        when(taskRepository.findByProjectIdAndId(projectId, taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDto result = commentService.updateComment(projectId, taskId, commentId, commentDto);

        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        assertEquals(commentId, result.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void deleteComment_ShouldDeleteComment_WhenExists() {
        Task task = new Task();
        Comment existingComment = new Comment(commentId, task, "member1", "This is a comment");
        when(taskRepository.findByProjectIdAndId(projectId, taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        assertDoesNotThrow(() -> commentService.deleteComment(projectId, taskId, commentId));

        verify(commentRepository).delete(existingComment);
    }

    @Test
    void getAllComments_ShouldReturnListOfComments_WhenExists() {
        Task task = new Task();
        Comment comment1 = new Comment(1L, task, "member1", "First comment");
        Comment comment2 = new Comment(2L, task, "member2", "Second comment");
        when(taskRepository.findByProjectIdAndId(projectId, taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findAllByTask(task)).thenReturn(List.of(comment1, comment2));

        List<CommentDto> result = commentService.getAllComments(projectId, taskId);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    /*@Test
    void registerComment_ShouldThrowException_WhenCommentDtoIsNull() {
        assertThrows(IllegalStateException.class, () -> {
            commentService.registerComment(projectId, taskId, null);
        });
    }*/

    @Test
    void getComment_ShouldThrowException_WhenCommentIdIsNull() {
        assertThrows(IllegalStateException.class, () -> {
            commentService.getComment(projectId, taskId, null);
        });
    }

    @Test
    void updateComment_ShouldThrowException_WhenCommentIdIsNull() {
        assertThrows(IllegalStateException.class, () -> {
            commentService.updateComment(projectId, taskId, null, commentDto);
        });
    }

    @Test
    void deleteComment_ShouldThrowException_WhenCommentIdIsNull() {
        assertThrows(IllegalStateException.class, () -> {
            commentService.deleteComment(projectId, taskId, null);
        });
    }

    @Test
    void updateComment_ShouldNotChangeContent_WhenContentIsNull() {
        Comment existingComment = new Comment(commentId, task, "member1", "Old content");
        commentDto = new CommentDto(commentId, task, "member1", null);

        when(taskRepository.findByProjectIdAndId(projectId, taskId)).thenReturn(Optional.of(task));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CommentDto result = commentService.updateComment(projectId, taskId, commentId, commentDto);

        assertNotNull(result);
        assertEquals("Old content", existingComment.getContent()); // content should not change
        assertEquals(commentId, result.getId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

}