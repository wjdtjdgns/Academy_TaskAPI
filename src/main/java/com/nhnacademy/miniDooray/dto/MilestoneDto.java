package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MilestoneDto {
    private Long id;
    private Project project;
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
