package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @DisplayName("findByProjectIdAndId - 존재하는 Task 조회")
    public void testFindByProjectIdAndId_Found() {
        // Given
        Project project = new Project();
        project.setName("Test Project");
        project.setAdminId("admin1");  // adminId 설정
        project.setStatus(Status.ACTIVATED);  // status 설정
        project = projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setProject(project);
        milestone.setTitle("Test Milestone");
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now());
        milestone = milestoneRepository.save(milestone);

        Task task = new Task();
        task.setTitle("Test Task");
        task.setContent("Test Content");
        task.setProject(project);
        task.setMilestone(milestone);
        task = taskRepository.save(task);

        // When
        Optional<Task> foundTaskOpt = taskRepository.findByProjectIdAndId(project.getId(), task.getId());

        // Then
        assertThat(foundTaskOpt).isPresent();
        Task foundTask = foundTaskOpt.get();
        assertThat(foundTask.getId()).isEqualTo(task.getId());
    }
}
