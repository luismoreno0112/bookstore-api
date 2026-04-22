package com.taller.bookstore.controller;

import com.taller.bookstore.dto.request.AuthorRequest;
import com.taller.bookstore.dto.response.ApiResponse;
import com.taller.bookstore.dto.response.AuthorResponse;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@Tag(name = "Autores", description = "Gestión de autores")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "Listar todos los autores")
    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(authorService.findAll(), "Autores obtenidos exitosamente"));
    }

    @Operation(summary = "Obtener autor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(authorService.findById(id), "Autor encontrado"));
    }

    @Operation(summary = "Listar libros de un autor")
    @GetMapping("/{id}/books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> findBooks(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(authorService.findBooksByAuthor(id), "Libros del autor obtenidos"));
    }

    @Operation(summary = "Crear autor", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<AuthorResponse>> create(@Valid @RequestBody AuthorRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(authorService.create(request), "Autor creado exitosamente", 201));
    }

    @Operation(summary = "Actualizar autor", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authorService.update(id, request), "Autor actualizado exitosamente"));
    }

    @Operation(summary = "Eliminar autor", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Autor eliminado exitosamente"));
    }
}
