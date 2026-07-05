package com.example.evento.models.DTO;

import lombok.Data;

@Data
public class MensajeriaDTO {
    private String correoEmisor;
    private String correoReceptor;
    private String fechaEnvio;
    private String asunto;
    private String cuerpoMensaje;
    private String estadoLectura;
}