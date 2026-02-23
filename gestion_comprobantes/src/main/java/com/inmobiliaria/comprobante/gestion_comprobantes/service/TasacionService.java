package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import com.inmobiliaria.comprobante.gestion_comprobantes.dto.TasacionDTO;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Cliente;
import com.inmobiliaria.comprobante.gestion_comprobantes.model.Tasacion;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.ClienteRepository;
import com.inmobiliaria.comprobante.gestion_comprobantes.repository.TasacionRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Service
public class TasacionService {

    @Autowired
    private SpringTemplateEngine templateEngine;
    private final TasacionRepository tasacionRepository;
    private final ClienteRepository clienteRepository;

    public TasacionService(TasacionRepository tasacionRepository, ClienteRepository clienteRepository) {
        this.tasacionRepository = tasacionRepository;
        this.clienteRepository = clienteRepository;
    }


    @Transactional
    public byte[] crearTasacion(TasacionDTO dto) throws Exception {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Tasacion t = new Tasacion();
        t.setCliente(cliente);
        t.setNombreSolicitante(cliente.getNombre());
        t.setDniSolicitante(cliente.getDni());

        t.setDomicilioSolicitante(dto.getDomicilioSolicitante());
        t.setCaracterSolicitante(dto.getCaracterSolicitante());
        t.setLocalidad(dto.getLocalidad());
        t.setCalleNumero(dto.getCalleNumero());
        t.setEntreCalles(dto.getEntreCalles());
        t.setPartido(dto.getPartido());
        t.setProvincia(dto.getProvincia());
        t.setNomenclaturaCatastral(dto.getNomenclaturaCatastral());
        t.setTitular(dto.getTitular());
        t.setPartida(dto.getPartida());
        t.setMatricula(dto.getMatricula());
        t.setTipoInmueble(dto.getTipoInmueble());
        t.setTipoInmuebleDetalle(dto.getTipoInmuebleDetalle());
        t.setDensidad(dto.getDensidad());

        t.setMedidasLote(dto.getMedidasLote());
        t.setSuperficieCubierta(dto.getSuperficieCubierta());
        t.setSuperficieSemicubierta(dto.getSuperficieSemicubierta());
        t.setUsoSuelo(dto.getUsoSuelo());
        t.setFos(dto.getFos());
        t.setFot(dto.getFot());
        t.setValorMercado(dto.getValorMercado());
        t.setObservaciones(dto.getObservaciones());
        t.setFechaCreacion(LocalDateTime.now());

        tasacionRepository.save(t);
        return generarPdfDesdeEntidad(t);
    }

    public Page<Tasacion> listarPaginado(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("fechaCreacion").descending());
        return tasacionRepository.findAll(pageable);
    }

    public Tasacion buscarPorId(Long id) {
        return tasacionRepository.findById(id).orElse(null);
    }

    private byte[] generarPdfDesdeEntidad(Tasacion t) throws Exception {
        Context context = new Context();
        context.setVariable("tasacion", t);

        String html = templateEngine.process("minuta-pdf", context);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();

        // CORRECCIÓN: Definimos la base URI para que encuentre el logo en static
        String baseUri = new ClassPathResource("/static/").getURL().toExternalForm();

        builder.withHtmlContent(html, baseUri);
        builder.toStream(os);
        builder.run();

        return os.toByteArray();
    }
}
