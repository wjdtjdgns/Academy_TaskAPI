package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByProjectIdAndId(Long projectId, Long taskId);
    List<Task> findAllByProjectId(Long projectId);
}
