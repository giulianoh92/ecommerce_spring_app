package com.ecommerce.main.error;

import java.util.HashMap;
import java.util.Map;

/**
 * ErrorMessages
 * esta clase se encarga de manejar los mensajes de error que se enviarán en la respuesta de la API
 * contiene un mapa con los mensajes de error y su respectivo código de estado HTTP
 */
public class ErrorMessages {
    private static final Map<Integer, ErrorDetail> errorMessages = new HashMap<>();

    static {
        errorMessages.put(4000, new ErrorDetail("Entrada no válida", 400));
        errorMessages.put(4001, new ErrorDetail("No autorizado", 401));
        errorMessages.put(4003, new ErrorDetail("Prohibido", 403));
        errorMessages.put(4004, new ErrorDetail("Recurso no encontrado", 404));
        errorMessages.put(4008, new ErrorDetail("Solicitud de tiempo de espera", 408));
        errorMessages.put(4010, new ErrorDetail("Conflicto", 409));
        errorMessages.put(4015, new ErrorDetail("Solicitud de entidad demasiado grande", 413));
        errorMessages.put(5000, new ErrorDetail("Error interno del servidor", 500));
        errorMessages.put(5001, new ErrorDetail("No implementado", 501));
        errorMessages.put(5002, new ErrorDetail("Bad Gateway", 502));
        errorMessages.put(5003, new ErrorDetail("Servicio no disponible", 503));
        errorMessages.put(5004, new ErrorDetail("Gateway Timeout", 504));
    }

    public static ErrorDetail getErrorDetail(int code) {
        return errorMessages.getOrDefault(code, new ErrorDetail("Unknown error", 500));
    }

    public static class ErrorDetail {
        private String message;
        private int httpStatusCode;

        public ErrorDetail(String message, int httpStatusCode) {
            this.message = message;
            this.httpStatusCode = httpStatusCode;
        }

        public String getMessage() {
            return message;
        }

        public int getHttpStatusCode() {
            return httpStatusCode;
        }
    }
}