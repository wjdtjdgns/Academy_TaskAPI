package com.nhnacademy.miniDooray.service.impl;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.dto.MilestoneRegisterDto;
import com.nhnacademy.miniDooray.entity.Milestone;
import com.nhnacademy.miniDooray.entity.Project;
import com.nhnacademy.miniDooray.exception.MilestoneNotFoundException;
import com.nhnacademy.miniDooray.exception.MilestoneTitleAlreadyExistsException;
import com.nhnacademy.miniDooray.exception.ProjectNotFoundException;
import com.nhnacademy.miniDooray.repository.MilestoneRepository;
import com.nhnacademy.miniDooray.repository.ProjectRepository;
import com.nhnacademy.miniDooray.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {

    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;

    @Override
    public MilestoneDto addMilestoneToProject(String userId, Long projectId, MilestoneRegisterDto milestoneDto) {
        if (userId == null || projectId == null || milestoneDto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        if (milestoneRepository.existsByProjectAndTitle(project, milestoneDto.getTitle())) {
            throw new MilestoneTitleAlreadyExistsException("Milestone with the same title already exists in project");
        }

        Milestone milestone = new Milestone();
        milestone.setTitle(milestoneDto.getTitle());
        milestone.setStartDate(milestoneDto.getStartDate());
        milestone.setEndDate(milestoneDto.getEndDate());
        milestone.setProject(project);

        milestoneRepository.save(milestone);
        return convertToDto(milestone);
    }

    @Override
    public List<MilestoneDto> getMilestonesByProjectId(String userId, Long projectId) {
        if (userId == null || projectId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        List<Milestone> milestones = milestoneRepository.findByProjectId(projectId);
        return milestones.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MilestoneDto updateMilestone(String userId, Long projectId, MilestoneDto milestoneDto) {
        if (userId == null || projectId == null || milestoneDto == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        Milestone milestone = milestoneRepository.findById(milestoneDto.getId())
                .orElseThrow(() -> new MilestoneNotFoundException("Milestone not found"));
        milestone.setTitle(milestoneDto.getTitle());
        milestone.setStartDate(milestoneDto.getStartDate());
        milestone.setEndDate(milestoneDto.getEndDate());
        milestoneRepository.save(milestone);
        return convertToDto(milestone);
    }

    @Override
    public void deleteProjectMilestone(String userId, Long projectId, Long milestoneId) {
        if (userId == null || projectId == null || milestoneId == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }

        if (!milestoneRepository.existsById(milestoneId)) {
            throw new MilestoneNotFoundException("Milestone not found");
        }

        milestoneRepository.deleteByProjectIdAndId(projectId, milestoneId);
    }

    private MilestoneDto convertToDto(Milestone milestone) {
        return new MilestoneDto(milestone.getId(), milestone.getProject(), milestone.getTitle(), milestone.getStartDate(), milestone.getEndDate());
    }
}

