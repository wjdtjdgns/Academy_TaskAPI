package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectUpdateDto {
    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}