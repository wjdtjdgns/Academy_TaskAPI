package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.TagDto;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.entity.Tag;
import com.nhnacademy.miniDooray.exception.ProjectNotFoundException;
import com.nhnacademy.miniDooray.exception.TagNameAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.TagNotFoundException;
import com.nhnacademy.miniDooray.repository.ProjectRepository;
import com.nhnacademy.miniDooray.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProjectRepository projectRepository;

    private Project project;
    private TagDto tagDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        project = new Project(1L, "admin1", "Project1", Status.ACTIVATED);
        tagDto = new TagDto(1L, project, "Tag1");
    }

    @Test
    void addTagToProject_success() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(tagRepository.existsByProjectAndName(any(Project.class), anyString())).thenReturn(false);
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TagDto result = tagService.addTagToProject("user1", 1L, tagDto);

        assertNotNull(result);
        assertEquals(tagDto.getName(), result.getName());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void addTagToProject_projectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> tagService.addTagToProject("user1", 1L, tagDto));
    }

    @Test
    void addTagToProject_tagNameAlreadyExists() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.of(project));
        when(tagRepository.existsByProjectAndName(any(Project.class), anyString())).thenReturn(true);

        assertThrows(TagNameAlreadyExistsException.class, () -> tagService.addTagToProject("user1", 1L, tagDto));
    }

    @Test
    void getTagsForProject_success() {
        when(tagRepository.findByProjectId(anyLong())).thenReturn(Collections.singletonList(new Tag()));

        List<TagDto> result = tagService.getTagsForProject("user1", 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tagRepository, times(1)).findByProjectId(anyLong());
    }

    @Test
    void updateProjectTag_success() {
        Tag tag = new Tag();
        tag.setId(1L);
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        TagDto result = tagService.updateProjectTag("user1", 1L, tagDto);

        assertEquals(tagDto.getName(), result.getName());
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void updateProjectTag_tagNotFound() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.updateProjectTag("user1", 1L, tagDto));
    }

    @Test
    void deleteProjectTag_success() {
        when(tagRepository.existsByProjectIdAndId(anyLong(), anyLong())).thenReturn(true);

        tagService.deleteProjectTag("user1", 1L, 1L);

        verify(tagRepository, times(1)).deleteByProjectIdAndId(anyLong(), anyLong());
    }

    @Test
    void deleteProjectTag_tagNotFound() {
        when(tagRepository.existsByProjectIdAndId(anyLong(), anyLong())).thenReturn(false);

        assertThrows(TagNotFoundException.class, () -> tagService.deleteProjectTag("user1", 1L, 1L));
    }

    // Null 체크 테스트
    @Test
    void addTagToProject_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> tagService.addTagToProject(null, 1L, tagDto));
        assertThrows(IllegalArgumentException.class, () -> tagService.addTagToProject("user1", null, tagDto));
        assertThrows(IllegalArgumentException.class, () -> tagService.addTagToProject("user1", 1L, null));
    }

    @Test
    void getTagsForProject_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> tagService.getTagsForProject(null, 1L));
        assertThrows(IllegalArgumentException.class, () -> tagService.getTagsForProject("user1", null));
    }

    @Test
    void updateProjectTag_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> tagService.updateProjectTag(null, 1L, tagDto));
        assertThrows(IllegalArgumentException.class, () -> tagService.updateProjectTag("user1", null, tagDto));
        assertThrows(IllegalArgumentException.class, () -> tagService.updateProjectTag("user1", 1L, null));
    }

    @Test
    void deleteProjectTag_nullParameters() {
        assertThrows(IllegalArgumentException.class, () -> tagService.deleteProjectTag(null, 1L, 1L));
        assertThrows(IllegalArgumentException.class, () -> tagService.deleteProjectTag("user1", null, 1L));
        assertThrows(IllegalArgumentException.class, () -> tagService.deleteProjectTag("user1", 1L, null));
    }
}
