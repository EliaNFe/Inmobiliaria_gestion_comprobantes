package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ClienteService;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/contratos")
public class ContratoController {

    private final ContratoService contratoService;
    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("contratos", contratoService.listarTodos());
        return "contratos";
    }

    @GetMapping("/nuevo")
    public String nuevoContrato(Model model) {
        model.addAttribute("contrato", new Contrato());
        model.addAttribute("inquilinos", clienteService.listarInquilinosActivos());
        model.addAttribute("propietarios", clienteService.listarPropietariosActivos());
        return "contratos-form";
    }

    @PostMapping("/{id}/inactivar")
    public String inactivar(@PathVariable Long id) {
        contratoService.cambiarEstado(id, false); // O el método que definas
        return "redirect:/contratos";
    }

    @PostMapping("/{id}/activar")
    public String activar(@PathVariable Long id) {
        contratoService.cambiarEstado(id, true);
        return "redirect:/contratos";
    }

    @GetMapping("/editar/{id}")
    public String editarContrato(@PathVariable Long id, Model model) {
        Contrato contrato = contratoService.buscarPorId(id);
        model.addAttribute("contrato", contrato);
        model.addAttribute("inquilinos", clienteService.listarInquilinosActivos());
        model.addAttribute("propietarios", clienteService.listarPropietariosActivos());

        return "contratos-form";
    }

    @PostMapping
    public String guardar(@ModelAttribute("contrato") Contrato contrato, Model model) {
        try {
            // VALIDACION: Evitar que el propietario sea la misma persona que el inquilino
            if (contrato.getPropietario() != null && contrato.getCliente() != null) {
                Long idPropietario = contrato.getPropietario().getId();
                Long idInquilino = contrato.getCliente().getId();

                if (idPropietario != null && idInquilino != null && idPropietario.equals(idInquilino)) {
                    model.addAttribute("error", "Un cliente no puede ser propietario e inquilino en el mismo contrato.");
                    model.addAttribute("inquilinos", clienteService.listarInquilinosActivos());
                    model.addAttribute("propietarios", clienteService.listarPropietariosActivos());
                    return "contratos-form";
                }
            }

            if (contrato.getId() != null) {
                Contrato contratoDB = contratoService.buscarPorId(contrato.getId());

                // Actualización de campos básicos
                if (contrato.getPropiedad() != null && !contrato.getPropiedad().isBlank()) {
                    contratoDB.setPropiedad(contrato.getPropiedad());
                }
                if (contrato.getMontoMensual() != null) {
                    contratoDB.setMontoMensual(contrato.getMontoMensual());
                }
                if (contrato.getMesesActualizacion() != null) {
                    contratoDB.setMesesActualizacion(contrato.getMesesActualizacion());
                }
                if (contrato.getFechaInicio() != null) {
                    contratoDB.setFechaInicio(contrato.getFechaInicio());
                }
                if (contrato.getFechaFin() != null) {
                    contratoDB.setFechaFin(contrato.getFechaFin());
                }

                // Actualización de Relaciones (Inquilino y Propietario)
                if (contrato.getCliente() != null && contrato.getCliente().getId() != null) {
                    contratoDB.setCliente(contrato.getCliente());
                }
                if (contrato.getPropietario() != null && contrato.getPropietario().getId() != null) {
                    contratoDB.setPropietario(contrato.getPropietario());
                }

                contratoService.guardar(contratoDB);
            } else {
                contrato.setActivo(true);
                contratoService.guardar(contrato);
            }

            return "redirect:/contratos";

        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar: " + e.getMessage());
            model.addAttribute("inquilinos", clienteService.listarInquilinosActivos());
            model.addAttribute("propietarios", clienteService.listarPropietariosActivos());
            return "contratos-form";
        }
    }

    @PostMapping("/{id}/actualizar-monto")
    public String actualizarMonto(@PathVariable Long id, @RequestParam BigDecimal porcentaje) {
        Contrato contrato = contratoService.buscarPorId(id);

        BigDecimal montoActual = contrato.getMontoMensual();

        BigDecimal incremento = montoActual.multiply(porcentaje)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal nuevoMonto = montoActual.add(incremento);

        contrato.setMontoMensual(nuevoMonto);
        contrato.setFechaUltimaActualizacion(LocalDate.now());

        contratoService.guardar(contrato);
        return "redirect:/contratos/" + id;
    }

    @GetMapping("/{id}/comprobante")
    public String generarComprobanteDirecto(@PathVariable Long id, Model model) {
        Contrato contrato = contratoService.buscarPorId(id);

        model.addAttribute("contrato", contrato);
        model.addAttribute("luz", BigDecimal.ZERO);
        model.addAttribute("gas", BigDecimal.ZERO);
        model.addAttribute("total", contrato.getMontoMensual());
        model.addAttribute("fechaHoy", LocalDate.now());
        model.addAttribute("nota", "");

        return "contrato-recibo";
    }

    @PostMapping("/{id}/generar-recibo")
    public String generarRecibo(
            @PathVariable Long id,
            @RequestParam BigDecimal luz,
            @RequestParam BigDecimal gas,
            @RequestParam(required = false) String nota,
            Model model) {

        Contrato contrato = contratoService.buscarPorId(id);

        BigDecimal total = contrato.getMontoMensual().add(luz).add(gas);

        model.addAttribute("contrato", contrato);
        model.addAttribute("luz", luz);
        model.addAttribute("gas", gas);
        model.addAttribute("nota", nota);
        model.addAttribute("total", total);
        model.addAttribute("fechaHoy", LocalDate.now());

        return "contrato-recibo";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        model.addAttribute("contrato", contratoService.buscarPorId(id));
        return "contrato-detalle";
    }

}