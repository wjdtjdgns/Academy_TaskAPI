package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.TagDto;

import java.util.List;

public interface TagService {
    TagDto addTagToProject(String userId, Long projectId, TagDto tagDto);
    List<TagDto> getTagsForProject(String userId, Long projectId);
    TagDto updateProjectTag(String userId, Long projectId, TagDto tagDto);
    void deleteProjectTag(String userId, Long projectId, Long tagId);
}
