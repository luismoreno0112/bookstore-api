package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthorRequest {

    @NotBlank(message = "El nombre del autor es obligatorio")
    private String name;

    private String biography;

    @Email(message = "Formato de email inválido")
    private String email;
}
