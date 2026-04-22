package com.taller.bookstore.exception.handler;

import com.taller.bookstore.dto.response.ApiErrorResponse;
import com.taller.bookstore.exception.custom.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiErrorResponse buildError(int code, String message, List<String> errors, HttpServletRequest request) {
        return ApiErrorResponse.builder()
                .status("error")
                .code(code)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(404, ex.getMessage(), List.of(ex.getMessage()), req));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(409, ex.getMessage(), List.of(ex.getMessage()), req));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiErrorResponse> handleStock(InsufficientStockException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(buildError(422, ex.getMessage(), List.of(ex.getMessage()), req));
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedAccessException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError(403, ex.getMessage(), List.of(ex.getMessage()), req));
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidOrderState(InvalidOrderStateException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(409, ex.getMessage(), List.of(ex.getMessage()), req));
    }

    @ExceptionHandler(AuthorHasBooksException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthorHasBooks(AuthorHasBooksException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(409, ex.getMessage(), List.of(ex.getMessage()), req));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(400, "Error de validación", fieldErrors, req));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError(403, "No tiene permisos para esta operación", List.of("No tiene permisos para esta operación"), req));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(401, "Autenticación requerida", List.of("Autenticación requerida"), req));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneral(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(500, "Error interno del servidor", List.of(ex.getMessage()), req));
    }
}
