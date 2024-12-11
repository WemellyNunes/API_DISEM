package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FinalizeDTO {
    @NotBlank
    private String content;

    private LocalDate dateContent;

    @NotNull
    private Long programing_id;
}
