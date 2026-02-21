package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }

    public void guardar(Contrato contrato) {
        contratoRepository.save(contrato);
    }

    public List<Contrato> porCliente(Long clienteId) {
        return contratoRepository.findByClienteId(clienteId);
    }
}