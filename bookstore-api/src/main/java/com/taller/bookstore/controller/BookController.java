// Feature: Book catalog - CRUD operations with pagination
package com.taller.bookstore.controller;

import com.taller.bookstore.dto.request.BookRequest;
import com.taller.bookstore.dto.response.ApiResponse;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Tag(name = "Libros", description = "Gestión del catálogo de libros")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Listar todos los libros con paginación")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BookResponse>>> findAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId) {

        Page<BookResponse> books;
        if (categoryId != null) {
            books = bookService.findByCategory(categoryId, pageable);
        } else if (authorId != null) {
            books = bookService.findByAuthor(authorId, pageable);
        } else {
            books = bookService.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.success(books, "Libros obtenidos exitosamente"));
    }

    @Operation(summary = "Obtener libro por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookService.findById(id), "Libro encontrado"));
    }

    @Operation(summary = "Crear nuevo libro", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> create(@Valid @RequestBody BookRequest request) {
        BookResponse response = bookService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Libro creado exitosamente", 201));
    }

    @Operation(summary = "Actualizar libro", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bookService.update(id, request), "Libro actualizado exitosamente"));
    }

    @Operation(summary = "Eliminar libro", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Libro eliminado exitosamente"));
    }
}