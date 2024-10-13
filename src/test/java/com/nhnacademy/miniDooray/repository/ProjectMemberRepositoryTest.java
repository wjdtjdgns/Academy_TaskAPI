package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.ProjectMember;
import com.nhnacademy.miniDooray.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectMemberRepositoryTest {

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByProjectId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        ProjectMember member = new ProjectMember();
        member.setMemberId("member1");
        member.setProject(project);
        projectMemberRepository.save(member);

        List<ProjectMember> members = projectMemberRepository.findByProjectId(project.getId());
        assertEquals(1, members.size());
        assertEquals("member1", members.get(0).getMemberId());
    }
}
