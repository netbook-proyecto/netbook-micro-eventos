package com.example.evento.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActualizarEventoRequest {

    @NotBlank(message = "El título del evento no puede estar vacío")
    private int idEventoCalendario;

    @NotBlank(message = "El título del evento no puede estar vacío")
    private String tituloEvento;

    @NotBlank(message = "La descripción del evento no puede estar vacía")
    private String descripcionEvento;

    @NotBlank(message = "El tipo de evento es obligatorio")
    private String tipoEvento;

    @NotBlank(message = "La visibilidad del evento es obligatoria")
    private String visibilidad;
}
