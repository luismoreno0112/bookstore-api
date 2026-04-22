package com.taller.bookstore.mapper;

import com.taller.bookstore.dto.request.BookRequest;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final AuthorMapper authorMapper;
    private final CategoryMapper categoryMapper;

    public Book toEntity(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setDescription(request.getDescription());
        // author y categories se asignan en el service
        return book;
    }

    public BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .price(book.getPrice())
                .stock(book.getStock())
                .description(book.getDescription())
                .author(book.getAuthor() != null ? authorMapper.toResponse(book.getAuthor()) : null)
                .categories(book.getCategories() != null
                        ? book.getCategories().stream().map(categoryMapper::toResponse).collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}
