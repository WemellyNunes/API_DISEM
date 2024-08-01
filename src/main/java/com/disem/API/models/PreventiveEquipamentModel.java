package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_PREVENTIVE_EQUIPAMENT")
public class PreventiveEquipamentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String overturning;

    @NotBlank
    private String period;

    @NotNull
    private String unit;

    @NotNull
    private String bloc;

    @NotNull
    private String ambient;

    @NotBlank
    private String brand;

    @NotNull
    private String type;

    @NotNull
    private String description;

    @NotNull
    private String model;

    @NotBlank
    private String condition;

    @NotBlank
    private String prevision;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDateTime modificationDate;
}
