package com.disem.API.dtos;

import com.disem.API.enums.OrdersServices.SystemEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PreventiveSystemDTO {
    @NotBlank
    private String period;

    @NotNull
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

    private UUID orderService_id;
}
