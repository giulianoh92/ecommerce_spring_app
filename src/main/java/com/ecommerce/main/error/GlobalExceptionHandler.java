package com.ecommerce.main.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


import jakarta.validation.*;


/**
 * GlobalExceptionHandler
 * esta clase se encarga de manejar las excepciones globales que se generen en la aplicación
 * contiene manejadores para errores personalizados, errores de validación y errores de enlace de datos
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> createErrorResponse(HttpStatus status, String code, String message, String detail) {
        Map<String, Object> body = new HashMap<>();
        body.put("code", code);
        body.put("message", message);
        body.put("detail", detail);
        return new ResponseEntity<>(body, status);
    }

    // Manejador para CustomError
    @ExceptionHandler(CustomError.class)
    public ResponseEntity<Object> handleCustomError(CustomError ex) {
        return createErrorResponse(
            HttpStatus.valueOf(ex.getHttpStatusCode()), 
            String.valueOf(ex.getCode()),
            ex.getMessage(),
            ex.getDetail()
        );
    }

    // Manejador para errores de validación
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append(violation.getMessage()).append("; ");
        }
        return new ResponseEntity<>(createErrorResponse("VALIDATION_ERROR", errorMessage.toString()), HttpStatus.BAD_REQUEST);
    }

    // Manejador para errores de enlace de datos (Binding)
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ResponseEntity<Object> handleBindingException(org.springframework.validation.BindException ex) {
        StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
        for (ObjectError error : ex.getAllErrors()) {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        }
        return new ResponseEntity<>(createErrorResponse("BINDING_ERROR", errorMessage.toString()), HttpStatus.BAD_REQUEST);
    }

    // Método común para estructurar la respuesta
    private Map<String, String> createErrorResponse(String code, String detail) {
        Map<String, String> body = new HashMap<>();
        body.put("code", code);
        body.put("message", "Ocurrió un error de validación.");
        body.put("detail", detail);
        return body;
    }
}
