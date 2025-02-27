package com.disem.API.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    @NotBlank
    private String name;

    private String role;

    private LocalDate createAt;
}
