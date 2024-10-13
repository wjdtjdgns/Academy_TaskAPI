package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MilestoneRepositoryTest {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByProjectId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("adminId");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setTitle("Milestone1");
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now().plusDays(7));
        milestoneRepository.save(milestone);

        List<Milestone> milestones = milestoneRepository.findByProjectId(project.getId());
        assertEquals(1, milestones.size());
        assertEquals("Milestone1", milestones.get(0).getTitle());
    }

    @Test
    void existsByProjectAndTitle() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("adminId");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setTitle("Milestone1");
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now().plusDays(7));
        milestoneRepository.save(milestone);

        boolean exists = milestoneRepository.existsByProjectAndTitle(project, "Milestone1");
        assertTrue(exists);

        exists = milestoneRepository.existsByProjectAndTitle(project, "NonExistingMilestone");
        assertFalse(exists);
    }

    @Test
    void deleteByProjectIdAndId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("adminId");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setTitle("Milestone1");
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now().plusDays(7));
        Milestone savedMilestone = milestoneRepository.save(milestone);

        milestoneRepository.deleteByProjectIdAndId(project.getId(), savedMilestone.getId());
        List<Milestone> milestones = milestoneRepository.findByProjectId(project.getId());
        assertTrue(milestones.isEmpty());
    }
}
