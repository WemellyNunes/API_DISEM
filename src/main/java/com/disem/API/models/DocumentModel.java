package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "TB_DOCUMENT")
public class DocumentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String nameFile;

    @NotNull
    private String description;

    @ManyToOne
    @JoinColumn(name = "orderService_id")
    private OrderServiceModel orderService;
}
