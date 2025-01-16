package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InstituteDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String acronym;

    private String unit;

    private String campus;

    private LocalDate created_at;


}
