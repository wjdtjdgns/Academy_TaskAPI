package com.nhnacademy.miniDooray.repository;

import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void findByProjectId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        // Tag 생성 및 저장
        Tag tag = new Tag();
        tag.setName("Tag1");
        tag.setProject(project);
        tagRepository.save(tag);

        // 프로젝트 ID로 태그 찾기
        List<Tag> tags = tagRepository.findByProjectId(project.getId());
        assertEquals(1, tags.size());
        assertEquals("Tag1", tags.get(0).getName());
    }

    @Test
    void existsByProjectAndName() {
        // Project 생성 및 필수 필드 설정
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Tag tag = new Tag();
        tag.setName("Tag1");
        tag.setProject(project);
        tagRepository.save(tag);

        boolean exists = tagRepository.existsByProjectAndName(project, "Tag1");
        assertTrue(exists);

        exists = tagRepository.existsByProjectAndName(project, "NonExistingTag");
        assertFalse(exists);
    }

    @Test
    void deleteByProjectIdAndId() {
        Project project = new Project();
        project.setName("Project1");
        project.setAdminId("admin1");
        project.setStatus(Status.ACTIVATED);
        projectRepository.save(project);

        Tag tag = new Tag();
        tag.setName("Tag1");
        tag.setProject(project);
        Tag savedTag = tagRepository.save(tag);

        tagRepository.deleteByProjectIdAndId(project.getId(), savedTag.getId());

        List<Tag> tags = tagRepository.findByProjectId(project.getId());
        assertTrue(tags.isEmpty());
    }
}
