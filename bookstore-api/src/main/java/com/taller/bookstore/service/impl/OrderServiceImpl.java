package com.taller.bookstore.service.impl;

import com.taller.bookstore.dto.request.OrderItemRequest;
import com.taller.bookstore.dto.request.OrderRequest;
import com.taller.bookstore.dto.response.OrderResponse;
import com.taller.bookstore.entity.*;
import com.taller.bookstore.exception.custom.*;
import com.taller.bookstore.mapper.OrderMapper;
import com.taller.bookstore.repository.BookRepository;
import com.taller.bookstore.repository.OrderRepository;
import com.taller.bookstore.repository.UserRepository;
import com.taller.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + userEmail));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            Book book = bookRepository.findById(itemRequest.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Libro", itemRequest.getBookId()));

            if (book.getStock() < itemRequest.getQuantity()) {
                throw new InsufficientStockException(book.getTitle(), itemRequest.getQuantity(), book.getStock());
            }

            BigDecimal subtotal = book.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(subtotal);

            OrderItem item = OrderItem.builder()
                    .book(book)
                    .quantity(itemRequest.getQuantity())
                    .subtotal(subtotal)
                    .build();
            items.add(item);
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .total(total)
                .items(items)
                .build();

        // Asignar referencia inversa
        items.forEach(item -> item.setOrder(order));

        // Decrementar stock al confirmar (PENDING → stock se reserva al crear)
        for (int i = 0; i < items.size(); i++) {
            Book book = items.get(i).getBook();
            book.setStock(book.getStock() - request.getItems().get(i).getQuantity());
            bookRepository.save(book);
        }

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> findMyOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + userEmail));
        return orderRepository.findByUserId(user.getId()).stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    public OrderResponse findById(Long id, String userEmail) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + userEmail));

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = order.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedAccessException();
        }

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(Long id, String userEmail) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + userEmail));

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = order.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new UnauthorizedAccessException();
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException(order.getStatus().name(), "cancelar");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(order.getStatus().name(), "cancelar");
        }

        // Restaurar stock al cancelar
        for (OrderItem item : order.getItems()) {
            Book book = item.getBook();
            book.setStock(book.getStock() + item.getQuantity());
            bookRepository.save(book);
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderMapper.toResponse(orderRepository.save(order));
    }
}
