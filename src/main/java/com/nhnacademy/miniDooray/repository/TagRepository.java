package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
     List<Tag> findByProjectId(Long projectId);
     void deleteByProjectIdAndId(Long projectId, Long id);
     boolean existsByProjectIdAndId(Long projectId, Long tagId);
     boolean existsByProjectAndName(Project project, String name);
}
