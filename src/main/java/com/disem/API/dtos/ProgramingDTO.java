package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ProgramingDTO {

    @NotNull
    private LocalDate datePrograming;

    @NotNull
    private LocalTime time;

    @NotBlank
    private String overseer;

    @NotBlank
    private String worker;

    @NotNull
    private Float cost;

    private String observation;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDateTime modificationDate;

    @NotBlank
    private String delayedDays;

    @NotNull
    private UUID orderService_id;
}
