package com.inmobiliaria.comprobante.gestion_comprobantes.controller;
import com.inmobiliaria.comprobante.gestion_comprobantes.dto.TasacionDTO;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Tasacion;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ClienteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.TasacionRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.TasacionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/tasacion")
public class TasacionController {
    private final TasacionService tasacionService;
    private final TasacionRepository tasacionRepository;
    private final ClienteRepository clienteRepository;

    public TasacionController(TasacionService tasacionService, TasacionRepository tasacionRepository, ClienteRepository clienteRepository) {
        this.tasacionService = tasacionService;
        this.tasacionRepository = tasacionRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/historial")
    public String historial(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Tasacion> pagina = tasacionService.listarPaginado(page);

        model.addAttribute("tasaciones", pagina.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagina.getTotalPages());
        return "tasacion-historial";
    }

    @GetMapping("/nueva")
    public String formularioTasacion(Model model) {
        model.addAttribute("tasacion", new TasacionDTO());
        List<Cliente> clientes = clienteRepository.findByActivoTrue();
        model.addAttribute("todosLosClientes", clientes);
        return "tasacion-form";
    }

    @PostMapping("/guardar")
    public ResponseEntity<byte[]> guardarYGenerar(@ModelAttribute TasacionDTO dto) {
        try {
            if (dto.getClienteId() == null) {
                return ResponseEntity.badRequest().body(null);
            }
            byte[] pdf = tasacionService.crearTasacion(dto);

            if (pdf == null || pdf.length == 0) {
                return ResponseEntity.internalServerError().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            String filename = "Minuta_Tasacion_" + LocalDate.now() + ".pdf";
            headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());
            headers.setContentLength(pdf.length);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            System.err.println("ERROR AL GENERAR PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
