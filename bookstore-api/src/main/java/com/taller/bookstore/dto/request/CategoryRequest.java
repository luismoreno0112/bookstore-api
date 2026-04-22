package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    private String description;
}
