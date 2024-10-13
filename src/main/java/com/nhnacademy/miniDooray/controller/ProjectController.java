package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.service.MilestoneService;
import com.nhnacademy.miniDooray.service.ProjectService;
import com.nhnacademy.miniDooray.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MilestoneService milestoneService;
    private final TagService tagService;

    // 1. 내가 속한 프로젝트 목록 가져오기 (Pageable)
    @Operation(summary = "내가 속한 프로젝트 목록 가져오기", description = "사용자 ID를 기반으로 속한 프로젝트 목록을 페이징 처리하여 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<ProjectPageResponse> getMyProjects(
            @RequestHeader("X-USER-ID") String userId,
            Pageable pageable) {
        Page<ProjectDto> projects = projectService.getProjectsByMemberId(userId, pageable);

        ProjectPageResponse response = new ProjectPageResponse();
        response.setContent(projects.getContent());
        response.setPageNumber(projects.getNumber());
        response.setPageSize(projects.getSize());
        response.setTotalElements(projects.getTotalElements());
        response.setTotalPages(projects.getTotalPages());
        response.setLast(projects.isLast());

        return ResponseEntity.ok().body(response);
    }

    // 2. 프로젝트 생성
    @Operation(summary = "프로젝트 생성", description = "관리자가 새로운 프로젝트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "409", description = "프로젝트 이름이 이미 존재함")
    })
    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @RequestHeader("X-USER-ID") String adminId,
            @Valid @RequestBody ProjectRegisterDto projectDto) {
        ProjectDto createdProject = projectService.createProject(adminId, projectDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    // 3. 프로젝트 멤버 추가
    @Operation(summary = "프로젝트 멤버 추가", description = "프로젝트에 멤버를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프로젝트 멤버 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/{projectId}/members")
    public ResponseEntity<ProjectDto> addProjectMembers(
            @RequestHeader("X-USER-ID") String adminId,
            @PathVariable Long projectId,
            @RequestBody List<String> memberIds) {
        ProjectDto project = projectService.addMembersToProject(adminId, projectId, memberIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    // 4. 단일 프로젝트 조회
    @Operation(summary = "단일 프로젝트 조회", description = "프로젝트 ID를 사용하여 특정 프로젝트 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDto> getProjectById(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId) {
        ProjectDto project = projectService.getProjectById(userId, projectId);
        return ResponseEntity.ok().body(project);
    }

    //TODO : 변경 중
    // 5. 프로젝트 이름 또는 상태 변경
    @Operation(summary = "프로젝트 업데이트", description = "프로젝트의 이름이나 상태를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 - 유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDto> updateProject(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectUpdateDto updatedProject) {
        ProjectDto project = projectService.updateProject(userId, projectId, updatedProject);
        return ResponseEntity.ok(project);
    }

    // 6. 프로젝트 태그 추가
    @Operation(summary = "프로젝트 태그 추가", description = "프로젝트에 태그를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "태그 추가 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @PostMapping("/{projectId}/tag")
    public ResponseEntity<TagDto> addTag(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @RequestBody TagDto tagDto) {
        TagDto createdTag = tagService.addTagToProject(userId, projectId, tagDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    // 7. 프로젝트 태그 조회
    @Operation(summary = "프로젝트 태그 조회", description = "프로젝트에 추가된 모든 태그를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @GetMapping("/{projectId}/tag")
    public ResponseEntity<List<TagDto>> getTags(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId) {
        List<TagDto> tags = tagService.getTagsForProject(userId, projectId);
        return ResponseEntity.ok(tags);
    }

    // 8. 프로젝트 태그 수정
    @Operation(summary = "프로젝트 태그 수정", description = "프로젝트에 추가된 태그를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    })
    @PutMapping("/{projectId}/tag")
    public ResponseEntity<TagDto> updateTag(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @RequestBody TagDto tagDto) {
        TagDto updatedTag = tagService.updateProjectTag(userId, projectId, tagDto);
        return ResponseEntity.ok(updatedTag);
    }

    // 9. 프로젝트 태그 삭제
    @Operation(summary = "프로젝트 태그 삭제", description = "프로젝트에 추가된 태그를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "태그 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    })
    @DeleteMapping("/{projectId}/tag/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @PathVariable Long tagId) {
        tagService.deleteProjectTag(userId, projectId, tagId);
        return ResponseEntity.ok().build();
    }

    // 10. 프로젝트 마일스톤 추가
    @Operation(summary = "프로젝트 마일스톤 추가", description = "프로젝트에 마일스톤을 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "마일스톤 추가 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @PostMapping("/{projectId}/milestone")
    public ResponseEntity<MilestoneDto> addMilestone(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @RequestBody MilestoneDto milestoneDto) {
        MilestoneDto createdMilestone = milestoneService.addMilestoneToProject(userId, projectId, milestoneDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMilestone);
    }

    // 11. 프로젝트 마일스톤 조회
    @Operation(summary = "프로젝트 마일스톤 조회", description = "프로젝트에 추가된 마일스톤을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마일스톤 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
    })
    @GetMapping("/{projectId}/milestone")
    public ResponseEntity<List<MilestoneDto>> getMilestones(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId) {
        List<MilestoneDto> milestones = milestoneService.getMilestonesByProjectId(userId, projectId);
        return ResponseEntity.ok(milestones);
    }

    // 12. 프로젝트 마일스톤 수정
    @Operation(summary = "프로젝트 마일스톤 수정", description = "프로젝트의 마일스톤을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마일스톤 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "마일스톤을 찾을 수 없음")
    })
    @PutMapping("/{projectId}/milestone")
    public ResponseEntity<MilestoneDto> updateMilestone(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @RequestBody MilestoneDto milestoneDto) {
        MilestoneDto updatedMilestone = milestoneService.updateMilestone(userId, projectId, milestoneDto);
        return ResponseEntity.ok(updatedMilestone);
    }

    // 13. 프로젝트 마일스톤 삭제
    @Operation(summary = "프로젝트 마일스톤 삭제", description = "프로젝트의 마일스톤을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마일스톤 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "마일스톤을 찾을 수 없음")
    })
    @DeleteMapping("/{projectId}/milestone/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @RequestHeader("X-USER-ID") String userId,
            @PathVariable Long projectId,
            @PathVariable Long milestoneId) {
        milestoneService.deleteProjectMilestone(userId, projectId, milestoneId);
        return ResponseEntity.ok().build();
    }

}
