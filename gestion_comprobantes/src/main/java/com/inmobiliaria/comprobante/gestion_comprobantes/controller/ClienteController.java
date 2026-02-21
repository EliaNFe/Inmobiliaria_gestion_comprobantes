package com.inmobiliaria.comprobante.gestion_comprobantes.controller;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "buscar", required = false) String buscar) {

        Pageable pageable = PageRequest.of(page, 10); // Probando con 2
        Page<Cliente> paginaClientes = clienteService.listarTodospaginable(buscar, pageable);

        model.addAttribute("clientes", paginaClientes);
        model.addAttribute("buscar", buscar);

        return "clientes";
    }

    @PostMapping("/{id}/inactivar")
    public String inactivar(@PathVariable Long id,
                            RedirectAttributes ra) {
        try {
            clienteService.inactivar(id);
            ra.addFlashAttribute("ok", "Cliente inactivado");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/activar")
    public String activar(@PathVariable Long id) {
        clienteService.activar(id);
        return "redirect:/clientes";
    }


    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Cliente cliente = clienteService.obtenerPorId(id);
            model.addAttribute("cliente", cliente);
            return "clientes-form";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "No se encontró el cliente");
            return "redirect:/clientes";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes-form";
    }

    @PostMapping
    public String guardarCliente(
            @Valid @ModelAttribute Cliente cliente,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "clientes-form"; // vuelve al form con errores
        }

        try {
            clienteService.guardar(cliente);
        } catch (Exception e) {
            model.addAttribute("errorGlobal", "El DNI ya existe");
            return "clientes-form";
        }

        return "redirect:/clientes";
    }
}

