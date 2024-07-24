package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ChecklistCorrectiveDTO {
    @NotBlank
    private String treatment;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    private LocalDateTime modificationDate;

    @NotNull
    private UUID programing_id;
}
