package com.taller.bookstore.mapper;

import com.taller.bookstore.dto.request.AuthorRequest;
import com.taller.bookstore.dto.response.AuthorResponse;
import com.taller.bookstore.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toEntity(AuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setEmail(request.getEmail());
        return author;
    }

    public AuthorResponse toResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .biography(author.getBiography())
                .email(author.getEmail())
                .build();
    }
}
