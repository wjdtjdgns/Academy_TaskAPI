package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.TagDto;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.entity.Tag;
import com.nhnacademy.miniDooray.entity.Task;
import com.nhnacademy.miniDooray.entity.TaskTag;
import com.nhnacademy.miniDooray.exception.ProjectNotFoundException;
import com.nhnacademy.miniDooray.exception.TagNameAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.TagNotFoundException;
import com.nhnacademy.miniDooray.repository.ProjectRepository;
import com.nhnacademy.miniDooray.repository.TagRepository;
import com.nhnacademy.miniDooray.repository.TaskRepository;
import com.nhnacademy.miniDooray.repository.TaskTagRepository;
import com.nhnacademy.miniDooray.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskTagRepository taskTagRepository;

    @Override
    public TagDto addTagToProject(String userId, Long projectId, TagDto tagDto) {
        if (userId == null || projectId == null || tagDto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        if (tagRepository.existsByProjectAndName(project, tagDto.getName())) {
            throw new TagNameAlreadyExistsException("Tag with the same name already exists in project");
        }

        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tag.setProject(project);
        tagRepository.save(tag);

        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        for(Task task : tasks){
            TaskTag taskTag = new TaskTag();
            taskTag.setTask(task);
            taskTag.setTag(tag);
            taskTag.setSelected(false);
            taskTagRepository.save(taskTag);
        }

        return convertToDto(tag);
    }

    @Override
    public List<TagDto> getTagsForProject(String userId, Long projectId) {
        if (userId == null || projectId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        List<Tag> tags = tagRepository.findByProjectId(projectId);
        return tags.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto updateProjectTag(String userId, Long projectId, TagDto tagDto) {
        if (userId == null || projectId == null || tagDto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Tag tag = tagRepository.findById(tagDto.getId())
                .orElseThrow(() -> new TagNotFoundException("Tag not found"));

        tag.setName(tagDto.getName());
        tagRepository.save(tag);

        return convertToDto(tag);
    }

    @Override
    public void deleteProjectTag(String userId, Long projectId, Long tagId) {
        if (userId == null || projectId == null || tagId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        if (!tagRepository.existsByProjectIdAndId(projectId, tagId)) {
            throw new TagNotFoundException("Tag not found");
        }

        tagRepository.deleteByProjectIdAndId(projectId, tagId);
    }

    private TagDto convertToDto(Tag tag) {
        return new TagDto(tag.getId(), tag.getProject(), tag.getName());
    }
}

