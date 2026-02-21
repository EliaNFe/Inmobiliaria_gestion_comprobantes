package com.inmobiliaria.comprobante.gestion_comprobantes.repository;


import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findByClienteId(Long clienteId);
}
