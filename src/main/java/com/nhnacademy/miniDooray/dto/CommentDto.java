package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.entity.Task;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentDto {
    private Long id;
    private Task task;
    @NotEmpty
    private String memberId;
    @NotEmpty
    private String content;
}