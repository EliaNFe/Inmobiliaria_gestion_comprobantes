package com.inmobiliaria.comprobante.gestion_comprobantes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasacionDTO {
    private Long clienteId;
    private String nombreSolicitante;
    private String dniSolicitante;
    private String domicilioSolicitante;
    private String caracterSolicitante;
    private String localidad;
    private String calleNumero;
    private String entreCalles;
    private String paraje;
    private String partido;
    private String provincia;
    private String callesManzana;
    private String nomenclaturaCatastral;
    private String titular;
    private String partida;
    private String matricula;
    private String tipoInmueble;
    private String tipoInmuebleDetalle;
    private String medidasLote;
    private String superficieCubierta;
    private String superficieSemicubierta;
    private String usoSuelo;
    private String fos;
    private String fot;
    private String densidad;
    private Double valorMercado;
    private String observaciones;
}
