package com.inmobiliaria.comprobante.gestion_comprobantes.repository;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Tasacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasacionRepository extends JpaRepository<Tasacion, Long> {
}