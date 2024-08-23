package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DispatchOSDTO {
    @NotBlank
    private String content;

    @NotNull
    private LocalDate dateContent;

    @NotNull
    private Long orderService_id;
}
