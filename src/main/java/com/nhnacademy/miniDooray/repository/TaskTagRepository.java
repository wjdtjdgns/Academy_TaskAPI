package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
    Optional<TaskTag> findByTaskIdAndTagId(Long taskId, Long tagId);
    List<TaskTag> findAllByTaskId(Long taskId);
}
