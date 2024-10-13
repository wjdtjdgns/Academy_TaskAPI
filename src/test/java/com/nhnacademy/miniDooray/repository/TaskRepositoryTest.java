package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Task;
import com.nhnacademy.miniDooray.entity.Status; // Status 열거형 임포트 추가
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private TaskTagRepository taskTagRepository;

    @Test
    @DisplayName("프로젝트 ID와 태스크 ID로 태스크 조회 테스트")
    void findByProjectIdAndId() {
        Project project = new Project();
        project.setName("Test Project");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setTitle("Test Milestone");
        milestone.setProject(project);
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now().plusDays(7));
        milestoneRepository.save(milestone); // milestone 저장

        Task task = new Task();
        task.setTitle("Test Task");
        task.setContent("Test Content");
        task.setProject(project);
        task.setMilestone(milestone); // milestone 설정
        taskRepository.save(task);

        Optional<Task> foundTask = taskRepository.findByProjectIdAndId(project.getId(), task.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    @DisplayName("프로젝트 ID와 태스크 ID로 태스크 조회 실패 테스트")
    void findByProjectIdAndId_NotFound() {
        Project project = new Project();
        project.setName("Test Project");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED); // status 설정 추가
        projectRepository.save(project);

        Optional<Task> foundTask = taskRepository.findByProjectIdAndId(project.getId(), 999L);
        assertThat(foundTask).isNotPresent();
    }
}