package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("dev")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Test
    void findByTaskAndId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setTitle("Milestone1");
        milestone.setProject(project);
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now().plusDays(10));
        milestoneRepository.save(milestone);

        Task task = new Task();
        task.setTitle("Task1");
        task.setContent("Sample task content");
        task.setProject(project);
        task.setMilestone(milestone);
        taskRepository.save(task);

        Comment comment = new Comment();
        comment.setContent("Test comment");
        comment.setMemberId("member1");
        comment.setTask(task);
        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> foundComment = commentRepository.findByTaskAndId(task, savedComment.getId());
        assertTrue(foundComment.isPresent());
        assertEquals("Test comment", foundComment.get().getContent());
        assertEquals("member1", foundComment.get().getMemberId());
    }

    @Test
    void findAllByTask() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Milestone milestone = new Milestone();
        milestone.setTitle("Milestone1");
        milestone.setProject(project);
        milestone.setStartDate(ZonedDateTime.now());
        milestone.setEndDate(ZonedDateTime.now().plusDays(10));
        milestoneRepository.save(milestone);

        Task task = new Task();
        task.setTitle("Task1");
        task.setContent("Sample task content");
        task.setProject(project);
        task.setMilestone(milestone);
        taskRepository.save(task);

        Comment comment1 = new Comment();
        comment1.setContent("Test comment 1");
        comment1.setMemberId("member1");
        comment1.setTask(task);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("Test comment 2");
        comment2.setMemberId("member2");
        comment2.setTask(task);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findAllByTask(task);
        assertEquals(2, comments.size());
        assertEquals("Test comment 1", comments.get(0).getContent());
        assertEquals("Test comment 2", comments.get(1).getContent());
        assertEquals("member1", comments.get(0).getMemberId());
        assertEquals("member2", comments.get(1).getMemberId());
    }
}
