package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }


    @Transactional
    public void cambiarEstado(Long id, boolean estado) {
        Contrato c = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        c.setActivo(estado);
        contratoRepository.save(c);
    }

    public void guardar(Contrato contrato) {
        if (contrato.getMesesActualizacion() <= 0) {
            throw new RuntimeException("Meses de actualización inválido");
        }

        if (contrato.getFechaFin().isBefore(contrato.getFechaInicio())) {
            throw new RuntimeException("La fecha fin no puede ser menor a la fecha inicio");
        }

        if (contrato.getMontoMensual().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Monto inválido");
        }
        contratoRepository.save(contrato);
    }

    public List<Contrato> porCliente(Long clienteId) {
        return contratoRepository.findByClienteId(clienteId);
    }
}