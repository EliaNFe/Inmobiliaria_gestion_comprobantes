package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Comprobante;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ComprobanteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comprobantes")
public class ComprobanteController {

    private final ComprobanteRepository comprobanteRepository;

    public ComprobanteController(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    @GetMapping
    public String listarComprobantes(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Comprobante> pagina = comprobanteRepository.findAll(
                PageRequest.of(page, 10, Sort.by("fechaEmision").descending())
        );

        model.addAttribute("comprobantes", pagina.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagina.getTotalPages());

        return "comprobante-lista";
    }
}