package com.example.evento.models.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AgregarEventoRequest {

    @NotNull(message = "El id del creador del evento es obligatorio")
    private Integer idCreador;

    @NotBlank(message = "El título del evento no puede estar vacío")
    private String tituloEvento;

    @NotBlank(message = "La descripción del evento no puede estar vacía")
    private String descripcionEvento;

    @NotBlank(message = "El tipo de evento es obligatorio")
    private String tipoEvento;

    @NotBlank(message = "La visibilidad del evento es obligatoria")
    private String visibilidad;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDate fechaEvento;
}