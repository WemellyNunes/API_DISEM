package com.disem.API.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "TB_NOTE")
public class NoteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "programing_id", nullable = false)
    private ProgramingModel programing;

    @PrePersist
    protected void onCreate() {
        this.date = LocalDate.now();
        this.time = LocalTime.now();
    }
}
