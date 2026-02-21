package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listarClientes(@RequestParam(required = false) String buscar,
                                 Model model) {

        List<Cliente> clientes;

        if (buscar != null && !buscar.isBlank()) {
            clientes = clienteService.buscarPorNombre(buscar);
        } else {
            clientes = clienteService.listarTodos();
        }

        model.addAttribute("clientes", clientes);
        model.addAttribute("buscar", buscar);

        return "clientes";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes-form";
    }

    @PostMapping
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }
}
