package com.taller.bookstore.service.impl;

import com.taller.bookstore.dto.request.BookRequest;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.entity.Author;
import com.taller.bookstore.entity.Book;
import com.taller.bookstore.entity.Category;
import com.taller.bookstore.exception.custom.DuplicateResourceException;
import com.taller.bookstore.exception.custom.ResourceNotFoundException;
import com.taller.bookstore.mapper.BookMapper;
import com.taller.bookstore.repository.AuthorRepository;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.repository.CategoryRepository;
import com.taller.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Libro", "ISBN", request.getIsbn());
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor", request.getAuthorId()));

        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        book.setCategories(resolveCategories(request.getCategoryIds()));

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));
        return bookMapper.toResponse(book);
    }

    @Override
    public Page<BookResponse> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toResponse);
    }

    @Override
    public Page<BookResponse> findByCategory(Long categoryId, Pageable pageable) {
        return bookRepository.findByCategoryId(categoryId, pageable).map(bookMapper::toResponse);
    }

    @Override
    public Page<BookResponse> findByAuthor(Long authorId, Pageable pageable) {
        return bookRepository.findByAuthorId(authorId, pageable).map(bookMapper::toResponse);
    }

    @Override
    @Transactional
    public BookResponse update(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));

        if (!book.getIsbn().equals(request.getIsbn()) && bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Libro", "ISBN", request.getIsbn());
        }

        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Autor", request.getAuthorId()));

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setDescription(request.getDescription());
        book.setAuthor(author);
        book.setCategories(resolveCategories(request.getCategoryIds()));

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro", id);
        }
        bookRepository.deleteById(id);
    }

    private List<Category> resolveCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        return ids.stream()
                .map(cid -> categoryRepository.findById(cid)
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría", cid)))
                .toList();
    }
}
