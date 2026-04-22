package com.taller.bookstore.exception.custom;

public class InvalidOrderStateException extends RuntimeException {
    public InvalidOrderStateException(String currentState, String action) {
        super("No se puede realizar la acción '" + action + "' sobre un pedido en estado " + currentState);
    }
}
