package com.ecommerce.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/****
 * Clase de control de errores personalizada
 * permite manejar los errores de rutas no encontradas
 * y enviar una respuesta personalizada
 */
@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNotFoundError(NoHandlerFoundException ex) {
        return new ResponseEntity<>("No encontrado!", HttpStatus.NOT_FOUND);
    }
}