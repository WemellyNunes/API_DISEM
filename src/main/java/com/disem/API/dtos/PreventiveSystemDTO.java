package com.disem.API.dtos;

import com.disem.API.enums.OrdersServices.SystemEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PreventiveSystemDTO {
    @NotBlank
    private String period;

    @NotBlank
    private SystemEnum system;

    @NotBlank
    private String edifice;

    private String observation;

    @NotNull
    private String prevision;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDateTime modificationDate;
}
