package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProjectId(Long projectId);
    boolean existsByMemberIdAndProjectId(String memberId, Long projectId);
}
