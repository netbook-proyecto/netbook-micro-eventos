package com.example.evento.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.evento.models.DTO.EventoCalendarioDTO;
import com.example.evento.models.request.ActualizarEventoRequest;
import com.example.evento.models.request.AgregarEventoRequest;
import com.example.evento.service.EventoService;

@RequestMapping("evento")
@RestController
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("")
    public List<EventoCalendarioDTO> obtenerTodosLosEventos() {
        return eventoService.obtenerTodosLosEventos();
    }

    @GetMapping("{idEventoCalendario}")
    public EventoCalendarioDTO obtenerEventoPorId(@PathVariable int idEventoCalendario) {
        return eventoService.obtenerEventoPorIdCalendario(idEventoCalendario);
    }

    @PostMapping("")
    public EventoCalendarioDTO crearEvento(@Valid @RequestBody AgregarEventoRequest nuevo) {
        return eventoService.agregarEvento(nuevo);
    }

    @PutMapping("")
    public EventoCalendarioDTO actualizarEvento(@Valid @RequestBody ActualizarEventoRequest nuevo) {
        return eventoService.actualizarEvento(nuevo);
    }

    @DeleteMapping("/{idEventoCalendario}")
    public String eliminarEvento(@PathVariable int idEventoCalendario) {
        return eventoService.eliminarEventoPorId(idEventoCalendario);
    }
}