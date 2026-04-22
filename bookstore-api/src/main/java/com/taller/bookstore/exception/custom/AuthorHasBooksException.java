package com.taller.bookstore.exception.custom;

public class AuthorHasBooksException extends RuntimeException {
    public AuthorHasBooksException(String authorName) {
        super("No se puede eliminar el autor '" + authorName + "' porque tiene libros asociados");
    }
}
