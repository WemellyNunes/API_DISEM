package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ImageDTO {

    @NotBlank
    private String nameFile;

    @NotNull
    private String description;

    @NotNull
    private UUID programing_id;
}
