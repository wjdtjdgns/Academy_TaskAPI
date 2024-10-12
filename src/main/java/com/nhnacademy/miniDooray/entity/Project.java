package com.nhnacademy.miniDooray.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Length(min = 1, max = 20)
    @Setter
    private String admin_id;

    @NotNull
    @Length(min = 1, max = 20)
    @Setter
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;
}
