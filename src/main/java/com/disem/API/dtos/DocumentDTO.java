package com.disem.API.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class DocumentDTO {

    @Column(name = "name_file", nullable = false)
    private String namefile;

    private String description;

    @NotNull
    private Long orderService_id;
}
