package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private BigDecimal alquiler;
    private BigDecimal luz;
    private BigDecimal gas;
    private BigDecimal total;

    @ManyToOne
    private Contrato contrato;
}
