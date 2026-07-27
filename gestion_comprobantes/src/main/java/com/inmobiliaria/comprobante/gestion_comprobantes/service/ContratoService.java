package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratoService {

    // --- Configuración de avisos del dashboard ---
    // Con cuántos días de anticipación avisamos que un contrato está por
    // actualizarse o por vencer. Cambiar acá afecta a todo el sistema.
    public static final int DIAS_AVISO_ACTUALIZACION = 20;
    public static final int DIAS_AVISO_VENCIMIENTO = 20;
    public static final int TAMANIO_PAGINA_DASHBOARD = 10;

    private final ContratoRepository contratoRepository;

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }

    /**
     * Fecha en la que corresponde la próxima actualización de un contrato:
     * desde la última actualización registrada, o desde el inicio del
     * contrato si todavía nunca se actualizó.
     */
    private LocalDate proximaActualizacion(Contrato c) {
        LocalDate base = (c.getFechaUltimaActualizacion() != null)
                ? c.getFechaUltimaActualizacion()
                : c.getFechaInicio();
        return base.plusMonths(c.getMesesActualizacion());
    }

    /**
     * Contratos activos cuya próxima actualización de precio ya venció o cae
     * dentro de los próximos DIAS_AVISO_ACTUALIZACION días. Siempre paginado:
     * como la fecha "próxima actualización" es calculada (no una columna de
     * la base), se filtra y ordena en memoria y luego se recorta la página,
     * igual que en listarPaginadosYFiltrados().
     */
    public Page<Contrato> listarProximosAActualizar(int page) {
        LocalDate limite = LocalDate.now().plusDays(DIAS_AVISO_ACTUALIZACION);

        List<Contrato> pendientes = contratoRepository.findByActivoTrue().stream()
                .filter(c -> !proximaActualizacion(c).isAfter(limite))
                .sorted(Comparator.comparing(this::proximaActualizacion))
                .toList();

        Pageable pageable = PageRequest.of(page, TAMANIO_PAGINA_DASHBOARD);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + TAMANIO_PAGINA_DASHBOARD, pendientes.size());
        List<Contrato> subLista = (start > pendientes.size()) ? List.of() : pendientes.subList(start, end);

        return new PageImpl<>(subLista, pageable, pendientes.size());
    }

    /**
     * Contratos activos que ya vencieron o vencen dentro de los próximos
     * DIAS_AVISO_VENCIMIENTO días. fechaFin es una columna real, así que
     * esta consulta se pagina directamente en la base de datos.
     */
    public Page<Contrato> listarProximosAVencer(int page) {
        LocalDate limite = LocalDate.now().plusDays(DIAS_AVISO_VENCIMIENTO);
        Pageable pageable = PageRequest.of(page, TAMANIO_PAGINA_DASHBOARD, Sort.by("fechaFin").ascending());
        return contratoRepository.findByActivoTrueAndFechaFinLessThanEqual(limite, pageable);
    }

    public Page<Contrato> listarPaginados(int page) {
        return contratoRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending()));
    }

    public Page<Contrato> listarPaginadosYFiltrados(int page, String buscar, Boolean activo) {
        List<Contrato> lista = contratoRepository.findAll(Sort.by("id").descending());

        if (buscar != null && !buscar.isBlank()) {
            String b = buscar.toLowerCase();
            lista = lista.stream()
                    .filter(c -> c.getPropiedad().toLowerCase().contains(b) ||
                            c.getCliente().getNombre().toLowerCase().contains(b) ||
                            c.getPropietario().getNombre().toLowerCase().contains(b))
                    .toList();
        }

        if (activo != null) {
            lista = lista.stream()
                    .filter(c -> c.isActivo() == activo)
                    .toList();
        }

        int start = (int) PageRequest.of(page, 10).getOffset();
        int end = Math.min((start + 10), lista.size());

        List<Contrato> subLista = (start > lista.size()) ? List.of() : lista.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(
                subLista, PageRequest.of(page, 10), lista.size()
        );
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