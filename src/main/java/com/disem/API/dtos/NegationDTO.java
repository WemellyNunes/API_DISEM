package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NegationDTO {

    @NotBlank
    private String content;

    private LocalDate date;

    @NotNull
    private Long orderService_id;
}
