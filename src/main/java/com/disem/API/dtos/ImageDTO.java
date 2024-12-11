package com.disem.API.dtos;

import com.disem.API.enums.OrdersServices.TypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class ImageDTO {

    @NotBlank
    private String nameFile;


    private String description;

    private TypeEnum type;

    @NotNull
    private Long programing_id;

}


