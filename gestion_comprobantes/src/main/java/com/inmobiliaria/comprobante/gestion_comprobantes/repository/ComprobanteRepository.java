package com.inmobiliaria.comprobante.gestion_comprobantes.repository;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
    @Query("SELECT COUNT(c) FROM Comprobante c WHERE c.fechaEmision >= CURRENT_DATE")
    long countComprobantesHoy();
}