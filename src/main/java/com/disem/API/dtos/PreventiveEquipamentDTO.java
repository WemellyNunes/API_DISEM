package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class PreventiveEquipamentDTO {
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
    private LocalDate creationDate;

    @NotNull
    private LocalDate modificationDate;

    private Long orderService_id;
}
