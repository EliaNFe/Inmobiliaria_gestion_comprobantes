package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Monto inválido")
    private BigDecimal montoMensual;

    @NotBlank(message = "La propiedad es obligatoria")
    private String propiedad;

    @NotNull(message = "Debe indicar cada cuántos meses actualiza")
    @Min(value = 1, message = "Mínimo 1 mes")
    @Max(value = 24, message = "Máximo 24 meses")
    private Integer mesesActualizacion;

    @NotNull(message = "Fecha inicio obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "Fecha fin obligatoria")
    private LocalDate fechaFin;

    private LocalDate fechaUltimaActualizacion;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public boolean necesitaActualizacion() {
        if (fechaUltimaActualizacion == null || mesesActualizacion == null) {
            return false;
        }

        LocalDate proxima =
                fechaUltimaActualizacion.plusMonths(mesesActualizacion);

        return !LocalDate.now().isBefore(proxima);
    }

}