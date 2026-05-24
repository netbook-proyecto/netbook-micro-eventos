package com.example.evento.models.entities;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "evento_calendario")
@Data
public class EventoCalendario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int idEventoCalendario;

    @Column(nullable=false)
    private String tituloEvento;
    
    @Column(nullable = false, length = 500)
    private String descripcionEvento;

    
    @Column(nullable = false, length = 50)
    private String tipoEvento;
    
    @Column(name = "visibilidad evento",nullable = false, length = 50)
    private String visibilidad;
    
    @Column(name = "fecha Evento", nullable = false, updatable= false)
    private LocalDateTime fechaEvento;

    @PrePersist
    protected void onCreate() {
        this.fechaEvento = LocalDateTime.now();
    }



}
