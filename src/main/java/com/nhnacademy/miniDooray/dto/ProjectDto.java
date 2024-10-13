package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ProjectDto {
    private Long id;
    private String adminId;
    private String name;
    private Status status;

    private List<String> memberIds;
}
