package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ComprobanteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ContratoService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    private final ContratoService contratoService;
    private final ComprobanteRepository comprobanteRepository;

    public DashboardController(ContratoService contratoService, ComprobanteRepository comprobanteRepository) {
        this.contratoService = contratoService;
        this.comprobanteRepository = comprobanteRepository;
    }

    @GetMapping("/dashboard")
    public String dashboardBridge(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/empleado/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(
            @RequestParam(defaultValue = "0") int pageActualizar,
            @RequestParam(defaultValue = "0") int pageVencer,
            Model model) {

        Page<Contrato> paginaActualizar = contratoService.listarProximosAActualizar(pageActualizar);
        Page<Contrato> paginaVencer = contratoService.listarProximosAVencer(pageVencer);

        model.addAttribute("contratosPorActualizar", paginaActualizar.getContent());
        model.addAttribute("totalActualizar", paginaActualizar.getTotalElements());
        model.addAttribute("currentPageActualizar", pageActualizar);
        model.addAttribute("totalPagesActualizar", paginaActualizar.getTotalPages());

        model.addAttribute("contratosPorVencer", paginaVencer.getContent());
        model.addAttribute("totalVencer", paginaVencer.getTotalElements());
        model.addAttribute("currentPageVencer", pageVencer);
        model.addAttribute("totalPagesVencer", paginaVencer.getTotalPages());

        model.addAttribute("diasAvisoActualizacion", ContratoService.DIAS_AVISO_ACTUALIZACION);
        model.addAttribute("diasAvisoVencimiento", ContratoService.DIAS_AVISO_VENCIMIENTO);

        return "dashboard";
    }

    @GetMapping("/empleado/dashboard")
    public String empleadoDashboard(Model model) {
        long hoy = comprobanteRepository.countComprobantesHoy();
        model.addAttribute("totalComprobantesHoy", hoy);
        return "dashboard-empleado";
    }
}