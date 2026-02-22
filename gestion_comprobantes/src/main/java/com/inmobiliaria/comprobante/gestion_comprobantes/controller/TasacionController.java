package com.inmobiliaria.comprobante.gestion_comprobantes.controller;

import com.inmobiliaria.comprobante.gestion_comprobantes.dto.TasacionDTO;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ClienteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.TasacionRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.service.TasacionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String historial(Model model) {
        // Ordenamos por fecha de creación para ver lo último primero
        model.addAttribute("tasaciones", tasacionRepository.findAll());
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
            // Validación básica
            if (dto.getClienteId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            // Generamos el PDF
            byte[] pdf = tasacionService.crearTasacion(dto);

            if (pdf == null || pdf.length == 0) {
                return ResponseEntity.internalServerError().build();
            }

            // Configuramos los headers para una respuesta de archivo limpia
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // El nombre del archivo con la fecha ayuda a la organización
            String filename = "Minuta_Tasacion_" + LocalDate.now() + ".pdf";
            headers.setContentDisposition(ContentDisposition.inline().filename(filename).build());

            // Importante para evitar que el navegador crea que es una página HTML
            headers.setContentLength(pdf.length);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            // Logueamos el error para saber qué pasó exactamente en la consola
            System.err.println("ERROR AL GENERAR PDF: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
