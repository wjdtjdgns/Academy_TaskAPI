package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Comment;
import com.nhnacademy.miniDooray.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByTaskAndId(Task task, Long id);
    List<Comment> findAllByTask(Task task);
}