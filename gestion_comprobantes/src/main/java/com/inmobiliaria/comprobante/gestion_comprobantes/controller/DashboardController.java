package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ComprobanteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ContratoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String adminDashboard(Model model) {
        model.addAttribute("vencimientos", contratoService.contarVencimientosMesActual());
        model.addAttribute("actualizaciones", contratoService.contarActualizacionesMesActual());
        return "dashboard";
    }

    @GetMapping("/empleado/dashboard")
    public String empleadoDashboard(Model model) {
        long hoy = comprobanteRepository.countComprobantesHoy();
        model.addAttribute("totalComprobantesHoy", hoy);
        return "dashboard-empleado";
    }
}