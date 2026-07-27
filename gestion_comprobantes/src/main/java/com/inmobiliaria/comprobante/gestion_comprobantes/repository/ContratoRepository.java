package com.inmobiliaria.comprobante.gestion_comprobantes.repository;


import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findByClienteId(Long clienteId);
    List<Contrato> findByActivoTrue();
    boolean existsByClienteIdAndActivoTrue(Long clienteId);
    boolean existsByPropietarioIdAndActivoTrue(Long propietarioId);

    // Contratos activos ya vencidos o que vencen hasta cierta fecha límite
    // (usado para el aviso de "próximos a vencer" del dashboard).
    Page<Contrato> findByActivoTrueAndFechaFinLessThanEqual(LocalDate hasta, Pageable pageable);
}
