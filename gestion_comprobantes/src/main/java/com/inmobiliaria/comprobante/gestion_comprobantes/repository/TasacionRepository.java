package com.inmobiliaria.comprobante.gestion_comprobantes.repository;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Tasacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasacionRepository extends JpaRepository<Tasacion, Long> {
    List<Tasacion> findAllByOrderByFechaCreacionDesc();
}