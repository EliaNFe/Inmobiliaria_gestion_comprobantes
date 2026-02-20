package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;

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
