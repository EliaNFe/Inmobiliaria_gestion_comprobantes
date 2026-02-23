package com.inmobiliaria.comprobante.gestion_comprobantes.controller;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Comprobante;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Contrato;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ComprobanteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ClienteService;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.ContratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/empleado")
public class EmpleadoController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ComprobanteRepository comprobanteRepository;
    @Autowired
    private ContratoService contratoService;


    @GetMapping("/clientes")
    public String listarClientes(Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "buscar", required = false) String buscar) {

        Pageable pageable = PageRequest.of(page, 10); // Probando con 2
        Page<Cliente> paginaClientes = clienteService.listarTodospaginable(buscar, pageable);

        model.addAttribute("clientes", paginaClientes);
        model.addAttribute("buscar", buscar);

        return "clientes";
    }

    @GetMapping("/comprobantes")
    public String historialComprobantes(Model model, @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<Comprobante> paginaComprobantes = comprobanteRepository.findAll(pageable);

        model.addAttribute("comprobantes", paginaComprobantes.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginaComprobantes.getTotalPages());
        model.addAttribute("totalItems", paginaComprobantes.getTotalElements());

        return "comprobante-lista";
    }

    @GetMapping("/contratos")
    public String listarContratosEmpleado(Model model,
                                          @RequestParam(defaultValue = "0") int page) {

        Page<Contrato> contratosPage = contratoService.listarPaginados(page);

        model.addAttribute("contratos", contratosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contratosPage.getTotalPages());
        model.addAttribute("totalItems", contratosPage.getTotalElements());

        return "contratos-lista-empleado";
    }
}