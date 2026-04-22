package com.taller.bookstore.repository;

import com.taller.bookstore.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"user", "items", "items.book"})
    List<Order> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user", "items", "items.book"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"user", "items", "items.book"})
    Optional<Order> findById(Long id);
}
