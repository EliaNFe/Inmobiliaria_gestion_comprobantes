package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamos con el contrato para saber de dónde viene
    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    // "Sacamos foto" de los datos en ese momento por si el contrato cambia después
    private String nombreInquilino;
    private String propiedadDireccion;
    private BigDecimal montoAbonado;

    private LocalDate fechaEmision; // Cuándo se generó el recibo
    private String periodoCorrespondiente; // Ej: "Marzo 2024"

    @PrePersist
    public void prePersist() {
        this.fechaEmision = LocalDate.now();
    }
}
