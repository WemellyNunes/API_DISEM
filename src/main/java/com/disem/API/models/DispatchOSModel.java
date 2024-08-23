package com.disem.API.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "TB_DISPATCH_OS")
public class DispatchOSModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String content;

    @NotNull
    private LocalDate dateContent;

    @ManyToOne
    @JoinColumn(name = "orderService_id", nullable = false)
    private OrderServiceModel orderService;
}
