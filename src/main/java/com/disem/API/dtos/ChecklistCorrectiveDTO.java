package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class ChecklistCorrectiveDTO {
    @NotBlank
    private String treatment;

    @NotNull
    private LocalDate creationDate;

    @NotNull
    private LocalDate modificationDate;

    @NotNull
    private Long programing_id;
}
