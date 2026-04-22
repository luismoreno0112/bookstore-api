package com.taller.bookstore.controller;

import com.taller.bookstore.dto.request.CategoryRequest;
import com.taller.bookstore.dto.response.ApiResponse;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.dto.response.CategoryResponse;
import com.taller.bookstore.service.CategoryService;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Gestión de categorías literarias")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Listar todas las categorías")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findAll(), "Categorías obtenidas exitosamente"));
    }

    @Operation(summary = "Obtener categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findById(id), "Categoría encontrada"));
    }

    @Operation(summary = "Listar libros de una categoría")
    @GetMapping("/{id}/books")
    public ResponseEntity<ApiResponse<List<BookResponse>>> findBooks(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findBooksByCategory(id), "Libros de la categoría obtenidos"));
    }

    @Operation(summary = "Crear categoría", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(categoryService.create(request), "Categoría creada exitosamente", 201));
    }

    @Operation(summary = "Actualizar categoría", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.update(id, request), "Categoría actualizada exitosamente"));
    }

    @Operation(summary = "Eliminar categoría", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Categoría eliminada exitosamente"));
    }
}
