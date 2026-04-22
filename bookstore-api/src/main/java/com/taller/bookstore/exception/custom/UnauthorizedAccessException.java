package com.taller.bookstore.exception.custom;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    public UnauthorizedAccessException() {
        super("No tiene permisos para acceder a este recurso");
    }
}
