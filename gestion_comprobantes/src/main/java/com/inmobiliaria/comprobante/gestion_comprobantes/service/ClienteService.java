package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ClienteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ContratoRepository contratoRepository;

    public Page<Cliente> listarTodospaginable(String buscar, Pageable pageable) {
        if (buscar != null && !buscar.isEmpty()) {
            return clienteRepository.findByNombreContainingIgnoreCase(buscar, pageable);
        }
        return clienteRepository.findAll(pageable);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public List<Cliente> listarInquilinosActivos() {
        return clienteRepository.findByEsInquilinoTrueAndActivoTrue();
    }

    public List<Cliente> listarPropietariosActivos() {
        return clienteRepository.findByEsPropietarioTrueAndActivoTrue();
    }

    public List<Cliente> listarTodosActivos() {
        return clienteRepository.findByActivoTrue();
    }

    public List<Cliente> buscarPorNombreActivos(String nombre) {
        return clienteRepository
                .findByNombreContainingIgnoreCaseAndActivoTrue(nombre);
    }

    @Transactional
    public void inactivar(Long id) {
        Cliente c = clienteRepository.findById(id).orElseThrow();


        boolean esInquilinoActivo = contratoRepository.existsByClienteIdAndActivoTrue(id);
        boolean esPropietarioActivo = contratoRepository.existsByPropietarioIdAndActivoTrue(id);

        if (esInquilinoActivo || esPropietarioActivo) {
            String rol = esInquilinoActivo ? "inquilino" : "propietario";
            throw new IllegalStateException(
                    "No se puede inactivar porque tiene contratos activos como " + rol);
        }

        c.setActivo(false);
    }

    @Transactional
    public void activar(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow();

        c.setActivo(true);
    }


    public Cliente guardar(Cliente cliente) {
        // Aquí podrías agregar lógica por defecto, ej: si no se marcó nada, es inquilino
        if (!cliente.isEsInquilino() && !cliente.isEsPropietario()) {
            cliente.setEsInquilino(true);
        }
        return clienteRepository.save(cliente);
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
}