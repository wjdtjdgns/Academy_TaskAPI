package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.entity.Tag;
import com.nhnacademy.miniDooray.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskTagDto {
    private Long id;
    private Tag tag;
    private Task task;
    private boolean isSelected = false;
}