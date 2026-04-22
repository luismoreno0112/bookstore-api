package com.taller.bookstore.service.impl;

import com.taller.bookstore.dto.request.AuthorRequest;
import com.taller.bookstore.dto.response.AuthorResponse;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.entity.Author;
import com.taller.bookstore.exception.custom.AuthorHasBooksException;
import com.taller.bookstore.exception.custom.ResourceNotFoundException;
import com.taller.bookstore.mapper.AuthorMapper;
import com.taller.bookstore.mapper.BookMapper;
import com.taller.bookstore.repository.AuthorRepository;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;

    @Override
    public AuthorResponse create(AuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse findById(Long id) {
        return authorMapper.toResponse(
                authorRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Autor", id))
        );
    }

    @Override
    public List<AuthorResponse> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        author.setName(request.getName());
        author.setBiography(request.getBiography());
        author.setEmail(request.getEmail());
        return authorMapper.toResponse(authorRepository.save(author));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));

        boolean hasBooks = !bookRepository.findByAuthorId(id, PageRequest.of(0, 1)).isEmpty();
        if (hasBooks) {
            throw new AuthorHasBooksException(author.getName());
        }

        authorRepository.deleteById(id);
    }

    @Override
    public List<BookResponse> findBooksByAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Autor", authorId);
        }
        return bookRepository.findByAuthorId(authorId, PageRequest.of(0, 100))
                .stream().map(bookMapper::toResponse).toList();
    }
}
