package com.inmobiliaria.comprobante.gestion_comprobantes.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // errores de base de datos (ej: DNI duplicado)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException ex, Model model) {
        model.addAttribute("error", "Dato duplicado o inválido en la base de datos.");
        return "error";
    }

    // cualquier error no controlado
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("error", "Ocurrió un error inesperado.");
        return "error";
    }
}