package com.example.evento.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.evento.models.entities.EventoCalendario;
import com.example.evento.models.request.ActualizarEventoRequest;
import com.example.evento.models.request.AgregarEventoRequest;
import com.example.evento.repository.EventoRepository;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    public List<EventoCalendario> obtenerTodosLosEventos() {
        return eventoRepository.findAll();
    }

    public EventoCalendario obtenerEventoPorIdCalendario(int idEventoCalendario) {
        EventoCalendario eventocalendario= eventoRepository.findById(idEventoCalendario).orElse(null);
        if (eventocalendario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado: ");
        }
        return eventocalendario;
    }


    public EventoCalendario agregarEvento(AgregarEventoRequest nuevo) {
        EventoCalendario eventooNuevo = new EventoCalendario();
        eventooNuevo.setTituloEvento(nuevo.getTituloEvento());
        eventooNuevo.setDescripcionEvento(nuevo.getDescripcionEvento());
        eventooNuevo.setTipoEvento(nuevo.getTipoEvento());
        eventooNuevo.setVisibilidad(nuevo.getVisibilidad());
        return eventoRepository.save(eventooNuevo);
    }

    public String eliminarEventoPorId(int idEventoCalendario) {
        if (eventoRepository.existsById(idEventoCalendario)) {
            eventoRepository.deleteById(idEventoCalendario);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado: ");
        }else {
            eventoRepository.deleteById(idEventoCalendario);
            return "Evento eliminado correctamente: ";
        }
    
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
        return eventoRepository.save(eventocalendario);
    }

}