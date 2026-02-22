package com.inmobiliaria.comprobante.gestion_comprobantes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasaciones")
public class Tasacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // SOLICITANTE
    private String nombreSolicitante;
    private String dniSolicitante;
    private String domicilioSolicitante;
    private String caracterSolicitante;

    // UBICACIÓN
    private String localidad;
    private String calleNumero;
    private String entreCalles;
    private String paraje;
    private String partido;
    private String provincia;

    // IDENTIFICACIÓN
    private String nomenclaturaCatastral;
    private String titular;
    private String partida;
    private String matricula;

    // TIPO E INDICADORES
    private String tipoInmueble; // vivienda, comercial, industrial, otros
    private String tipoInmuebleDetalle;
    private String medidasLote;
    private String superficieCubierta;
    private String superficieSemicubierta;
    private String usoSuelo;
    private String fos;
    private String fot;
    private String densidad;

    // VALOR Y COMENTARIOS
    private Double valorMercado;
    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // DATOS PROFESIONAL (Podrían ser fijos o venir del usuario logueado)
    private String cuitProfesional;
    private String telefonoProfesional;


}