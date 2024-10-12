package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
