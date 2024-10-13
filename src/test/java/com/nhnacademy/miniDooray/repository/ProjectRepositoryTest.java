package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.ProjectMember;
import com.nhnacademy.miniDooray.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Test
    void findProjectsByMemberId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        ProjectMember member = new ProjectMember();
        member.setMemberId("member1");
        member.setProject(project);
        projectMemberRepository.save(member);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> projectsPage = projectRepository.findProjectsByMemberId("member1", pageable);

        assertEquals(1, projectsPage.getTotalElements());
        assertEquals("Project1", projectsPage.getContent().get(0).getName());
    }

    @Test
    void existsByName() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        boolean exists = projectRepository.existsByName("Project1");
        assertTrue(exists);

        exists = projectRepository.existsByName("NonExistingProject");
        assertFalse(exists);
    }
}
