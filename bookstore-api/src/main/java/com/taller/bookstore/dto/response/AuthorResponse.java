package com.taller.bookstore.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthorResponse {
    private Long id;
    private String name;
    private String biography;
    private String email;
}
