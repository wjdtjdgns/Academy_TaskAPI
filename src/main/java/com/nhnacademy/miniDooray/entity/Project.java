package com.nhnacademy.miniDooray.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Setter
    private String adminId;

    @NotNull
    @Length(min = 1, max = 20)
    @Setter
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;

}
