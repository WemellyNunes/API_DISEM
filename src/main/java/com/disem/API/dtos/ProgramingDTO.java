package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


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
    private LocalDate creationDate;

    @NotNull
    private LocalDate modificationDate;

    @NotBlank
    private String delayedDays;

    private String active;

    @NotNull
    private Long orderService_id;
}
