package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
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