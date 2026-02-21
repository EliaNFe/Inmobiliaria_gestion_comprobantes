package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montoMensual;
    private String propiedad;
    private Integer mesesActualizacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaUltimaActualizacion;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}