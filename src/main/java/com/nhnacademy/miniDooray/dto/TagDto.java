package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TagDto {
    private Long id;
    private Project project;
    private String name;
}
