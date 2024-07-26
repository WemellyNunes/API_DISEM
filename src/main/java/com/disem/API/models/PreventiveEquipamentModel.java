package com.disem.API.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode
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
