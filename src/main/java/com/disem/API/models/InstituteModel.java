package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "TB_INSTITUTE")
public class InstituteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String acronym;

    private String unit;

    private String campus;

    private LocalDate created_at;
}
