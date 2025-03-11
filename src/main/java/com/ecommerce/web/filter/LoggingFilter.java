package com.ecommerce.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * Clase de filtro para el registro de las peticiones HTTP
 * permite el registro de las peticiones HTTP entrantes y salientes
 * en la consola de la aplicación
 */
@Component
public class LoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        logger.info("Incoming request: {} {}", httpRequest.getMethod(), httpRequest.getRequestURI());
        logHeaders(httpRequest);

        chain.doFilter(request, response);

        logger.info("Outgoing response: {}", httpResponse.getStatus());
        logHeaders(httpResponse);
    }

    @Override
    public void destroy() {
    }

    private void logHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("Request Header: {} = {}", headerName, headerValue);
        }
    }

    private void logHeaders(HttpServletResponse response) {
        for (String headerName : response.getHeaderNames()) {
            String headerValue = response.getHeader(headerName);
            logger.info("Response Header: {} = {}", headerName, headerValue);
        }
    }
}