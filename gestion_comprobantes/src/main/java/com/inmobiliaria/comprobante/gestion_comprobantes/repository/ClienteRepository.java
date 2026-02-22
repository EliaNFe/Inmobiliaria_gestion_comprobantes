package com.inmobiliaria.comprobante.gestion_comprobantes.repository;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {


    Page<Cliente> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    List<Cliente> findAll();
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    List<Cliente> findByActivoTrue();
    List<Cliente> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    List<Cliente> findByEsInquilinoTrueAndActivoTrue();
    List<Cliente> findByEsPropietarioTrueAndActivoTrue();
    Optional<Cliente> findByDni(String dni);

}