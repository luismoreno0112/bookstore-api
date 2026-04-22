package com.taller.bookstore.service.impl;

import com.taller.bookstore.dto.request.CategoryRequest;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.dto.response.CategoryResponse;
import com.taller.bookstore.entity.Category;
import com.taller.bookstore.exception.custom.ResourceNotFoundException;
import com.taller.bookstore.mapper.BookMapper;
import com.taller.bookstore.mapper.CategoryMapper;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.repository.CategoryRepository;
import com.taller.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse findById(Long id) {
        return categoryMapper.toResponse(
                categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría", id))
        );
    }

    @Override
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría", id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookResponse> findBooksByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoría", categoryId);
        }
        return bookRepository.findByCategoryId(categoryId, PageRequest.of(0, 100))
                .stream().map(bookMapper::toResponse).toList();
    }
}
