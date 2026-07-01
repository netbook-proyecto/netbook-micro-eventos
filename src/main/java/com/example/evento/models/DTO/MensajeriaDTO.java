package com.example.evento.models.DTO;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MensajeriaDTO{
    private Integer idMensaje;
    private LocalDate fechaEnvio;
    private String asunto;
    private String cuerpoMensaje;
    private String estadoLectura;

}
