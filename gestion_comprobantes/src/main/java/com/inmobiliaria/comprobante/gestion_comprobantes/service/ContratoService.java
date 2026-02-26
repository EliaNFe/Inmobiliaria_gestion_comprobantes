package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }


    public long contarVencimientosMesActual() {
        LocalDate inicio = LocalDate.now().withDayOfMonth(1);
        LocalDate fin = LocalDate.now().withDayOfMonth(inicio.lengthOfMonth());
        return contratoRepository.countByFechaFinBetweenAndActivoTrue(inicio, fin);
    }

    public long contarActualizacionesMesActual() {
        return contratoRepository.findByActivoTrue().stream()
                .filter(c -> {
                    if (c.getFechaUltimaActualizacion() == null) return true;
                    return c.getFechaUltimaActualizacion().plusMonths(c.getMesesActualizacion())
                            .isBefore(LocalDate.now().plusDays(1));
                }).count();
    }

    public Page<Contrato> listarPaginados(int page) {
        return contratoRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        Contrato c = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        c.setActivo(estado);
        contratoRepository.save(c);
    }

    @Transactional // Agregamos Transactional también al guardar
    public void guardar(Contrato contrato) {

        if (contrato.getMontoMensual() == null || contrato.getMontoMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto mensual debe ser mayor a cero.");
        }

        if (contrato.getMontoDeposito() == null || contrato.getMontoDeposito().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El Deposito debe ser mayor a cero.");
        }


        if (contrato.getFechaInicio() == null || contrato.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias.");
        }

        if (contrato.getFechaFin().isBefore(contrato.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser anterior a la de inicio.");
        }


        if (contrato.getMesesActualizacion() == null || contrato.getMesesActualizacion() <= 0) {
            throw new IllegalArgumentException("El intervalo de actualización debe ser de al menos 1 mes.");
        }


        if (contrato.getId() == null) {
            contrato.setActivo(true);
        }

        contratoRepository.save(contrato);
    }


    public Contrato buscarPorId(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado: " + id));
    }

    public List<Contrato> porCliente(Long clienteId) {
        return contratoRepository.findByClienteId(clienteId);
    }
}