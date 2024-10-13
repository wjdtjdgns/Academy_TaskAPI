package com.nhnacademy.miniDooray.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskTagResponse {
    private Long tagId;
    private boolean isSelected;
}
