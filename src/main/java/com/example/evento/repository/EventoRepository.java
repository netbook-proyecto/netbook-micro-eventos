package com.example.evento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.evento.models.entities.EventoCalendario;

    
@Repository
public interface  EventoRepository extends JpaRepository<EventoCalendario, Integer> {

}
