package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.entity.*;
import com.nhnacademy.miniDooray.exception.IdAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.IdNotFoundException;
import com.nhnacademy.miniDooray.repository.*;
import com.nhnacademy.miniDooray.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final MilestoneRepository milestoneRepository;
    private final TaskTagRepository taskTagRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;

    @Override
    public TaskDto registerTask(String userId ,Long projectId, TaskRegisterDto taskDto) {
        if(userId == null){
            throw new IdNotFoundException("존재하지 않는 아이디입니다.");
        }

        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setContent(taskDto.getContent());

        Milestone milestone =milestoneRepository.findById(taskDto.getMilestoneId())
                .orElseThrow(() -> new IdNotFoundException("마일스톤 id를 찾을 수 없습니다."));
        task.setMilestone(milestone);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IdNotFoundException("존재하지 않는 프로젝트 아이디입니다."));
        task.setProject(project);

        taskRepository.save(task);

        List<Tag> tags = tagRepository.findByProjectId(projectId);
        for (Tag tag : tags) {
            TaskTag taskTag = new TaskTag();
            taskTag.setTag(tag);
            taskTag.setTask(task);
            taskTag.setSelected(false);
            taskTagRepository.save(taskTag);
        }

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

    @Override
    public List<TaskDto> getTasksByProjectId(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }

        List<Task> tasks = taskRepository.findAllByProjectId(projectId);
        return tasks.stream().map(this::convertToDto).toList();
    }

    @Override
    public List<TaskTagResponse> registerTags(String userId, Long projectId, Long taskId, TaskTagRequest taskTagRequest) {
        taskRepository.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new IdNotFoundException("존재하지 않습니다."));

        List<TaskTagResponse> responseList = new ArrayList<>();

        for (Long tagId : taskTagRequest.getTagIds()) {
            TaskTag taskTag = taskTagRepository.findByTaskIdAndTagId(taskId, tagId)
                    .orElseThrow(() -> new IdNotFoundException("존재하지 않습니다."));

            taskTag.setSelected(true);

            taskTagRepository.save(taskTag);

            responseList.add(new TaskTagResponse(taskTag.getId(), true));
        }

        return responseList;
    }

    @Override
    public List<TaskTag> getTagByTaskId(Long taskId) {
        List<TaskTag> allByTaskId = taskTagRepository.findAllByTaskId(taskId);
        return allByTaskId;
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
