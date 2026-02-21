package com.inmobiliaria.comprobante.gestion_comprobantes.repository;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

}