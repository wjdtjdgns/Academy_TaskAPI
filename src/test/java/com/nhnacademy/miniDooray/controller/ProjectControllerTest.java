package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.entity.Status;
import com.nhnacademy.miniDooray.service.MilestoneService;
import com.nhnacademy.miniDooray.service.ProjectService;
import com.nhnacademy.miniDooray.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private MilestoneService milestoneService;

    @MockBean
    private TagService tagService;

    @Test
    @DisplayName("GET - /projects (내가 속한 프로젝트 목록 가져오기)")
    void testGetMyProjects() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProjectDto> page = new PageImpl<>(Collections.singletonList(new ProjectDto(1L, "adminId", "ProjectName", Status.ACTIVATED, List.of())), pageable, 1);
        when(projectService.getProjectsByMemberId(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/projects")
                        .header("X-USER-ID", "userId")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("ProjectName"))
                .andExpect(jsonPath("$.content[0].adminId").value("adminId"))
                .andExpect(jsonPath("$.content[0].status").value("ACTIVATED"));
    }

    @Test
    @DisplayName("POST - /projects (프로젝트 생성)")
    void testCreateProject() throws Exception {
        ProjectDto projectDto = new ProjectDto(1L, "adminId", "ProjectName", Status.ACTIVATED, List.of());
        when(projectService.createProject(anyString(), any(ProjectRegisterDto.class))).thenReturn(projectDto);

        mockMvc.perform(post("/projects")
                        .header("X-USER-ID", "adminId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ProjectName\",\"status\":\"ACTIVATED\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ProjectName"))
                .andExpect(jsonPath("$.adminId").value("adminId"))
                .andExpect(jsonPath("$.status").value("ACTIVATED"));
    }

    @Test
    @DisplayName("POST - /projects 실패 - 잘못된 요청")
    void testCreateProject_Failure_InvalidInput() throws Exception {
        mockMvc.perform(post("/projects")
                        .header("X-USER-ID", "adminId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"status\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST - /projects/{projectId}/members (프로젝트 멤버 추가)")
    void testAddProjectMembers() throws Exception {
        ProjectDto projectDto = new ProjectDto(1L, "adminId", "ProjectName", Status.ACTIVATED, List.of("memberId"));
        when(projectService.addMembersToProject(anyString(), anyLong(), anyList())).thenReturn(projectDto);

        mockMvc.perform(post("/projects/1/members")
                        .header("X-USER-ID", "adminId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"memberId\"]"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ProjectName"))
                .andExpect(jsonPath("$.adminId").value("adminId"))
                .andExpect(jsonPath("$.memberIds[0]").value("memberId"));
    }

    @Test
    @DisplayName("GET - /projects/{projectId} (단일 프로젝트 조회)")
    void testGetProjectById() throws Exception {
        ProjectDto projectDto = new ProjectDto(1L, "adminId", "ProjectName", Status.ACTIVATED, List.of());
        when(projectService.getProjectById(anyString(), anyLong())).thenReturn(projectDto);

        mockMvc.perform(get("/projects/1")
                        .header("X-USER-ID", "userId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ProjectName"))
                .andExpect(jsonPath("$.adminId").value("adminId"))
                .andExpect(jsonPath("$.status").value("ACTIVATED"));
    }

    @Test
    @DisplayName("PUT - /projects/{projectId} (프로젝트 이름 또는 상태 변경)")
    void testUpdateProject() throws Exception {
        ProjectDto updatedProjectDto = new ProjectDto(1L, "adminId", "UpdatedName", Status.CLOSED, List.of());

        when(projectService.updateProject(anyString(), anyLong(), any(ProjectUpdateDto.class)))
                .thenReturn(updatedProjectDto);

        mockMvc.perform(put("/projects/1")
                        .header("X-USER-ID", "userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UpdatedName\",\"status\":\"CLOSED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    @DisplayName("POST - /projects/{projectId}/tag (프로젝트 태그 추가)")
    void testAddTag() throws Exception {
        TagDto tagDto = new TagDto(1L, null, "TagName");
        when(tagService.addTagToProject(anyString(), anyLong(), any(TagDto.class))).thenReturn(tagDto);

        mockMvc.perform(post("/projects/1/tag")
                        .header("X-USER-ID", "userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TagName\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("TagName"));
    }

    @Test
    @DisplayName("GET - /projects/{projectId}/tag (프로젝트 태그 조회)")
    void testGetTags() throws Exception {
        TagDto tagDto = new TagDto(1L, null, "TagName");
        when(tagService.getTagsForProject(anyString(), anyLong())).thenReturn(List.of(tagDto));

        mockMvc.perform(get("/projects/1/tag")
                        .header("X-USER-ID", "userId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("TagName"));
    }

    @Test
    @DisplayName("PUT - /projects/{projectId}/tag (프로젝트 태그 수정)")
    void testUpdateTag() throws Exception {
        TagDto updatedTagDto = new TagDto(1L, null, "UpdatedTag");
        when(tagService.updateProjectTag(anyString(), anyLong(), any(TagDto.class))).thenReturn(updatedTagDto);

        mockMvc.perform(put("/projects/1/tag")
                        .header("X-USER-ID", "userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"UpdatedTag\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UpdatedTag"));
    }

    @Test
    @DisplayName("DELETE - /projects/{projectId}/tag/{tagId} (프로젝트 태그 삭제)")
    void testDeleteTag() throws Exception {
        doNothing().when(tagService).deleteProjectTag(anyString(), anyLong(), anyLong());

        mockMvc.perform(delete("/projects/1/tag/1")
                        .header("X-USER-ID", "userId"))
                .andExpect(status().isOk());
    }

    /*@Test
    @DisplayName("POST - /projects/{projectId}/milestone (프로젝트 마일스톤 추가)")
    void testAddMilestone() throws Exception {
        MilestoneDto milestoneDto = new MilestoneDto(1L, null, "MilestoneTitle", null, null);
        when(milestoneService.addMilestoneToProject(anyString(), anyLong(), any(MilestoneDto.class))).thenReturn(milestoneDto);

        mockMvc.perform(post("/projects/1/milestone")
                        .header("X-USER-ID", "userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"MilestoneTitle\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("MilestoneTitle"));
    }*/

    @Test
    @DisplayName("GET - /projects/{projectId}/milestone (프로젝트 마일스톤 조회)")
    void testGetMilestones() throws Exception {
        MilestoneDto milestoneDto = new MilestoneDto(1L, null, "MilestoneTitle", null, null);
        when(milestoneService.getMilestonesByProjectId(anyString(), anyLong())).thenReturn(List.of(milestoneDto));

        mockMvc.perform(get("/projects/1/milestone")
                        .header("X-USER-ID", "userId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("MilestoneTitle"));
    }

    @Test
    @DisplayName("PUT - /projects/{projectId}/milestone (프로젝트 마일스톤 수정)")
    void testUpdateMilestone() throws Exception {
        MilestoneDto updatedMilestoneDto = new MilestoneDto(1L, null, "UpdatedMilestone", null, null);
        when(milestoneService.updateMilestone(anyString(), anyLong(), any(MilestoneDto.class))).thenReturn(updatedMilestoneDto);

        mockMvc.perform(put("/projects/1/milestone")
                        .header("X-USER-ID", "userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"UpdatedMilestone\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("UpdatedMilestone"));
    }

    @Test
    @DisplayName("DELETE - /projects/{projectId}/milestone/{milestoneId} (프로젝트 마일스톤 삭제)")
    void testDeleteMilestone() throws Exception {
        doNothing().when(milestoneService).deleteProjectMilestone(anyString(), anyLong(), anyLong());

        mockMvc.perform(delete("/projects/1/milestone/1")
                        .header("X-USER-ID", "userId"))
                .andExpect(status().isOk());
    }
}