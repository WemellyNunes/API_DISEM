package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UnitDTO {
    @NotBlank
    private String unit;

    private String campus;
}
