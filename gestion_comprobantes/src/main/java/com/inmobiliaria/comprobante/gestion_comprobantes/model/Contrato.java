package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal alquilerActual;
    private Integer mesesActualizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaUltimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}