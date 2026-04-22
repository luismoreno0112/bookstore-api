package com.taller.bookstore.repository;

import com.taller.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    @EntityGraph(attributePaths = {"author", "categories"})
    Optional<Book> findById(Long id);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.author.id = :authorId")
    Page<Book> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

    Page<Book> findAll(Pageable pageable);
}
