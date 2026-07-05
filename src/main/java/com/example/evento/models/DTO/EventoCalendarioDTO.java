package com.example.evento.models.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class EventoCalendarioDTO {
    private int idEventoCalendario;
    private int idCreador;
    private String tituloEvento;
    private String descripcionEvento;
    private String tipoEvento;
    private String visibilidad;
    private LocalDate fechaEvento;
}