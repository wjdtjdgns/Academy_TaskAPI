package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
