package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TeamDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String role;

    private String status;

    private LocalDate created_at;
}
