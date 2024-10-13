package com.nhnacademy.miniDooray.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MilestoneRegisterDto {
    @NotNull
    @Length(min = 1, max = 50)
    private String title;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
