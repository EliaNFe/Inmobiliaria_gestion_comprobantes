package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "cliente",
        uniqueConstraints = @UniqueConstraint(columnNames = "dni")
)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$",
            message = "El nombre solo puede contener letras")
    private String nombre;


    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,10}",
            message = "El DNI debe tener solo números (7-10)")
    @Column(unique = true)
    private String dni;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9+\\- ]{6,20}$",
            message = "Teléfono inválido")
    private String telefono;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "cliente")
    private List<Contrato> contratos;

}
