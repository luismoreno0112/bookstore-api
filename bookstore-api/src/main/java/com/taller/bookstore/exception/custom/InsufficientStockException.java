package com.taller.bookstore.exception.custom;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String bookTitle, int requested, int available) {
        super("Stock insuficiente para '" + bookTitle + "'. Solicitado: " + requested + ", disponible: " + available);
    }
}
