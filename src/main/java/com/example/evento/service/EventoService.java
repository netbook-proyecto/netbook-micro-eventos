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

        EventoCalendario eventoGuardado = eventoRepository.save(eventoNuevo);

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

    private void enviarMensajeNotificacion(EventoCalendario evento) {
        try {
            MensajeriaDTO mensajeDTO = new MensajeriaDTO();
            mensajeDTO.setFechaEnvio(LocalDate.now().toString());
            mensajeDTO.setAsunto("Nuevo evento creado: " + evento.getTituloEvento());
            mensajeDTO.setCuerpoMensaje("Se ha creado el evento con la siguiente descripción: " + evento.getDescripcionEvento());
            mensajeDTO.setEstadoLectura("NO_LEIDO");

            mensajeriaWebClient.post()
                .uri("/mensajerias")
                .bodyValue(mensajeDTO)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

            System.out.println("ÉXITO: Mensaje enviado al microservicio de mensajería.");

        } catch (Exception e) {
            System.err.println("ERROR: No se pudo contactar al microservicio de mensajería. Motivo: " + e.getMessage());
        }
    }
}