package com.ibereciartua.app.config.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * This class handles all exceptions thrown by the application and returns a proper response.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Default application crash response

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                Map<String, Object> response = new HashMap<>();
                response.put("message", errorAttributes.get("message"));
                return response;
            }
        };
    }

    @ExceptionHandler(value = { BadRequestErrorException.class })
    protected ResponseEntity<Object> handleBadRequest(final RuntimeException e, final WebRequest request) {
        logger.warn("Bad Request: " + e.getMessage());
        return handleExceptionInternal(e, new ApiError(e), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException e, final WebRequest request) {
        logger.error("Invalid arguments", e);
        return handleExceptionInternal(e, new ApiError(e), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { InternalServerErrorException.class })
    protected ResponseEntity<Object> handleInternalServerError(final RuntimeException e, final WebRequest request) {
        logger.error("Internal Server Error", e);
        return handleExceptionInternal(e, new ApiError(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { RuntimeException.class })
    protected ResponseEntity<Object> handleRuntimeException(final RuntimeException e, final WebRequest request) {
        logger.error("Internal Server Error", e);
        return handleExceptionInternal(e, new ApiError(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
