package com.disem.API.dtos;

import com.disem.API.enums.OrdersServices.TypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ImageDTO {

    @NotBlank
    private String nameFile;


    private String description;

    private TypeEnum type;

    private LocalDateTime createdAt;

    @NotNull
    private Long programing_id;

}


