package com.disem.API.models;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "os_history")
public class OsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_service_id", nullable = false)
    private Long orderServiceId;

    private String action;

    private String performedBy;

    private LocalDateTime performedAt;

}
