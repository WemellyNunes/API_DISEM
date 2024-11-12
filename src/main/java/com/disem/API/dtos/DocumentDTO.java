package com.disem.API.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class DocumentDTO {
    @NotNull
    private String nameFile;

    private String description;

    @NotNull
    private Long orderService_id;
}
