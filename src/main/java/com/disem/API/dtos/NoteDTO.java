package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NoteDTO {
    @NotBlank
    private String content;

    private LocalDate date;

    private LocalTime time;

    private Long programing_id;
}
