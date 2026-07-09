package com.example.evento.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.evento.models.DTO.EventoCalendarioDTO;
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

    @Value("${app.notificaciones.correo-sistema}")
    private String correoSistema;

    public List<EventoCalendarioDTO> obtenerTodosLosEventos() {
        return eventoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public EventoCalendarioDTO obtenerEventoPorIdCalendario(int idEventoCalendario) {
        EventoCalendario evento = buscarEventoOLanzarError(idEventoCalendario);
        return convertirADTO(evento);
    }

    public EventoCalendarioDTO agregarEvento(AgregarEventoRequest nuevo) {
        EventoCalendario eventoNuevo = new EventoCalendario();
        eventoNuevo.setIdCreador(nuevo.getIdCreador());
        eventoNuevo.setTituloEvento(nuevo.getTituloEvento());
        eventoNuevo.setDescripcionEvento(nuevo.getDescripcionEvento());
        eventoNuevo.setTipoEvento(nuevo.getTipoEvento());
        eventoNuevo.setVisibilidad(nuevo.getVisibilidad());
        eventoNuevo.setFechaEvento(nuevo.getFechaEvento());

        EventoCalendario eventoGuardado = eventoRepository.save(eventoNuevo);

        enviarMensajeNotificacion(eventoGuardado);

        return convertirADTO(eventoGuardado);
    }

    public String eliminarEventoPorId(int idEventoCalendario) {
        if (!eventoRepository.existsById(idEventoCalendario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado con ID: " + idEventoCalendario);
        }
        eventoRepository.deleteById(idEventoCalendario);
        return "Evento eliminado correctamente.";
    }

    public EventoCalendarioDTO actualizarEvento(ActualizarEventoRequest nuevoEvento) {
        EventoCalendario evento = buscarEventoOLanzarError(nuevoEvento.getIdEventoCalendario());

        evento.setIdCreador(nuevoEvento.getIdCreador());
        evento.setTituloEvento(nuevoEvento.getTituloEvento());
        evento.setDescripcionEvento(nuevoEvento.getDescripcionEvento());
        evento.setTipoEvento(nuevoEvento.getTipoEvento());
        evento.setVisibilidad(nuevoEvento.getVisibilidad());
        evento.setFechaEvento(nuevoEvento.getFechaEvento());

        EventoCalendario eventoActualizado = eventoRepository.save(evento);
        return convertirADTO(eventoActualizado);
    }

    private EventoCalendario buscarEventoOLanzarError(int idEventoCalendario) {
        return eventoRepository.findById(idEventoCalendario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Evento no encontrado: " + idEventoCalendario));
    }

    private EventoCalendarioDTO convertirADTO(EventoCalendario evento) {
        EventoCalendarioDTO dto = new EventoCalendarioDTO();
        dto.setIdEventoCalendario(evento.getIdEventoCalendario());
        dto.setIdCreador(evento.getIdCreador());
        dto.setTituloEvento(evento.getTituloEvento());
        dto.setDescripcionEvento(evento.getDescripcionEvento());
        dto.setTipoEvento(evento.getTipoEvento());
        dto.setVisibilidad(evento.getVisibilidad());
        dto.setFechaEvento(evento.getFechaEvento());
        return dto;
    }

    private void enviarMensajeNotificacion(EventoCalendario evento) {
        try {
            MensajeriaDTO mensajeDTO = new MensajeriaDTO();
            mensajeDTO.setCorreoEmisor(correoSistema);
            mensajeDTO.setCorreoReceptor(correoSistema);
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