package com.disem.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DocumentDTO {
    @NotNull
    private String nameFile;

    @NotNull
    private String description;

    @NotNull
    private UUID orderService_id;
}
