package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
}
