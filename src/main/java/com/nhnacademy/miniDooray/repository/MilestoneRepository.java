package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByProjectId(Long projectId);
    void deleteByProjectIdAndId(Long projectId, Long id);
    boolean existsByProjectAndTitle(Project project, String title);
}
