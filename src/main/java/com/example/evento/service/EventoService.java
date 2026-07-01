package com.example.evento.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.evento.models.DTO.MensajeriaDTO;
import com.example.evento.models.entities.EventoCalendario;
import com.example.evento.models.request.ActualizarEventoRequest;
import com.example.evento.models.request.AgregarEventoRequest;
import com.example.evento.repository.EventoRepository;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    // 1. Inyectamos el cliente web que apunta al puerto de tu compañero
    @Autowired
    @Qualifier("mensajeriaWebClient")
    private WebClient mensajeriaWebClient;

    public List<EventoCalendario> obtenerTodosLosEventos() {
        return eventoRepository.findAll();
    }

    public EventoCalendario obtenerEventoPorIdCalendario(int idEventoCalendario) {
        EventoCalendario eventocalendario = eventoRepository.findById(idEventoCalendario).orElse(null);
        if (eventocalendario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado: " + idEventoCalendario);
        }
        return eventocalendario;
    }

    public EventoCalendario agregarEvento(AgregarEventoRequest nuevo) {
        EventoCalendario eventoNuevo = new EventoCalendario();
        eventoNuevo.setTituloEvento(nuevo.getTituloEvento());
        eventoNuevo.setDescripcionEvento(nuevo.getDescripcionEvento());
        eventoNuevo.setTipoEvento(nuevo.getTipoEvento());
        eventoNuevo.setVisibilidad(nuevo.getVisibilidad());
        eventoNuevo.setFechaEvento(nuevo.getFechaEvento());
        
        // Guardamos el evento en tu base de datos primero
        EventoCalendario eventoGuardado = eventoRepository.save(eventoNuevo);

        // 2. Llamamos al método que se comunica con el otro microservicio
        enviarMensajeNotificacion(eventoGuardado);

        return eventoGuardado;
    }

    public String eliminarEventoPorId(int idEventoCalendario) {
        if (!eventoRepository.existsById(idEventoCalendario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + idEventoCalendario);
        }
        eventoRepository.deleteById(idEventoCalendario);
        return "Evento eliminado correctamente.";
    }

    public EventoCalendario actualizarEvento(ActualizarEventoRequest nuevoEvento) {
        EventoCalendario eventocalendario = eventoRepository.findById(nuevoEvento.getIdEventoCalendario()).orElse(null);
        if (eventocalendario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado: " + nuevoEvento.getIdEventoCalendario());
        }
        eventocalendario.setTituloEvento(nuevoEvento.getTituloEvento());
        eventocalendario.setDescripcionEvento(nuevoEvento.getDescripcionEvento());
        eventocalendario.setTipoEvento(nuevoEvento.getTipoEvento());
        eventocalendario.setVisibilidad(nuevoEvento.getVisibilidad());
        eventocalendario.setFechaEvento(nuevoEvento.getFechaEvento());
        return eventoRepository.save(eventocalendario);
    }

    // --- NUEVO MÉTODO DE COMUNICACIÓN ---
    private void enviarMensajeNotificacion(EventoCalendario evento) {
        try {
            // Preparamos el DTO con los datos que necesita tu compañero
            MensajeriaDTO mensajeDTO = new MensajeriaDTO();
            mensajeDTO.setIdMensaje(evento.getIdEventoCalendario()); // Usamos el ID del evento como referencia
            mensajeDTO.setFechaEnvio(LocalDate.now());
            mensajeDTO.setAsunto("Nuevo evento creado: " + evento.getTituloEvento());
            mensajeDTO.setCuerpoMensaje("Se ha creado el evento con la siguiente descripción: " + evento.getDescripcionEvento());
            mensajeDTO.setEstadoLectura("NO_LEIDO");

            // Hacemos la petición POST al microservicio de Mensajería
            mensajeriaWebClient.post()
                .uri("") // <--- AQUÍ va la ruta final del controller de tu compañero (ej. "/enviar" o "")
                .bodyValue(mensajeDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // Síncrono: espera a que se envíe

            System.out.println("ÉXITO: Mensaje enviado al microservicio de mensajería.");

        } catch (Exception e) {
            // Si el servicio de tu compañero está apagado o falla, capturamos el error
            // para que TU evento se guarde de todas formas y no le devuelva un error 500 a tu usuario.
            System.err.println("ERROR: No se pudo contactar al microservicio de mensajería. Motivo: " + e.getMessage());
        }
    }
}