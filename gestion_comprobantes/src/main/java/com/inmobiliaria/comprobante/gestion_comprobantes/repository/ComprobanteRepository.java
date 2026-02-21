package com.inmobiliaria.comprobante.gestion_comprobantes.repository;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
}