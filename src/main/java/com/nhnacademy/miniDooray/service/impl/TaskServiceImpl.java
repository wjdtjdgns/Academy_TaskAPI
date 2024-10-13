package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.dto.TagDto;
import com.nhnacademy.miniDooray.dto.TaskDto;
import com.nhnacademy.miniDooray.dto.TaskTagDto;
import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Tag;
import com.nhnacademy.miniDooray.entity.Task;
import com.nhnacademy.miniDooray.entity.TaskTag;
import com.nhnacademy.miniDooray.exception.IdAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.IdNotFoundException;
import com.nhnacademy.miniDooray.repository.MilestoneRepository;
import com.nhnacademy.miniDooray.repository.TaskRepository;
import com.nhnacademy.miniDooray.repository.TaskTagRepository;
import com.nhnacademy.miniDooray.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MilestoneRepository milestoneRepository;
    private final TaskTagRepository taskTagRepository;

    @Override
    public TaskDto registerTask(TaskDto taskDto) {
        if (taskDto == null) {
            throw new IllegalArgumentException("TaskDto cannot be null");
        }

        if (taskRepository.existsById(taskDto.getId())) {
            throw new IdAlreadyExistsException("Task id가 이미 존재합니다. " + taskDto.getId());
        }

        Task task = new Task(
                taskDto.getId(),
                taskDto.getMilestone(),
                taskDto.getProject(),
                taskDto.getTitle(),
                taskDto.getContent()
        );

        taskRepository.save(task);

        return convertToDto(task);
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskDto taskDto) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        if (taskDto == null) {
            throw new IllegalArgumentException("TaskDto cannot be null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFoundException("해당 ID가 없습니다."));

        if (taskDto.getMilestone() != null) {
            task.setMilestone(taskDto.getMilestone());
        }

        if (taskDto.getProject() != null) {
            task.setProject(taskDto.getProject());
        }

        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }

        if (taskDto.getContent() != null) {
            task.setContent(taskDto.getContent());
        }

        taskRepository.save(task);

        return convertToDto(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFoundException("해당 ID가 없습니다."));

        taskRepository.delete(task);
    }

    @Override
    public Page<TaskDto> getTasks(int page, int size) {
        if (page < 0 || size < 0) {
            throw new IllegalArgumentException("Page and size must be non-negative");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasksPage = taskRepository.findAll(pageable);

        return tasksPage.map(this::convertToDto);
    }

    @Override
    public TaskDto getTask(Long taskId) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFoundException("해당 ID가 없습니다."));

        return convertToDto(task);
    }

    @Override
    public TaskDto updateMilestone(Long taskId, Long milestoneId, MilestoneDto milestoneDto) {
        if (taskId == null) {
            throw new IllegalArgumentException("Task ID cannot be null");
        }
        if (milestoneId == null) {
            throw new IllegalArgumentException("Milestone ID cannot be null");
        }
        if (milestoneDto == null) {
            throw new IllegalArgumentException("MilestoneDto cannot be null");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IdNotFoundException("해당 ID가 없습니다."));

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new IdNotFoundException("해당 ID가 없습니다."));

        task.setMilestone(milestone);

        taskRepository.save(task);

        return convertToDto(task);
    }

    @Override
    public TaskTagDto updateTag(Long taskTagId, TaskDto taskDto, TagDto tagDto) {
        if (taskTagId == null) {
            throw new IllegalArgumentException("TaskTag ID cannot be null");
        }
        if (taskDto == null) {
            throw new IllegalArgumentException("TaskDto cannot be null");
        }
        if (tagDto == null) {
            throw new IllegalArgumentException("TagDto cannot be null");
        }

        TaskTag taskTag = taskTagRepository.findById(taskTagId)
                .orElseThrow(() -> new IdNotFoundException("해당 ID가 없습니다."));

        Tag tag = new Tag(
                tagDto.getId(),
                tagDto.getProject(),
                tagDto.getName()
        );

        Task task = new Task(
                taskDto.getId(),
                taskDto.getMilestone(),
                taskDto.getProject(),
                taskDto.getTitle(),
                taskDto.getContent()
        );

        taskTag.setTag(tag);
        taskTag.setTask(task);

        taskTagRepository.save(taskTag);

        return convertToDto(taskTag);
    }

    private TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getMilestone(),
                task.getProject(),
                task.getTitle(),
                task.getContent()
        );
    }

    private TaskTagDto convertToDto(TaskTag taskTag) {
        return new TaskTagDto(
                taskTag.getId(),
                taskTag.getTag(),
                taskTag.getTask(),
                taskTag.isSelected()
        );
    }
}
