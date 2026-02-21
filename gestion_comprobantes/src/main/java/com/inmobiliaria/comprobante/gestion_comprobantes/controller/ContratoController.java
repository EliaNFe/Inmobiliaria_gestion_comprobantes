package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ClienteService;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ContratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("clientes", clienteService.listarTodos());
        return "contratos-form";
    }

    @PostMapping
    public String guardar(@ModelAttribute Contrato contrato) {
        contratoService.guardar(contrato);
        return "redirect:/contratos";
    }
}