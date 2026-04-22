package com.taller.bookstore.service;

import com.taller.bookstore.dto.request.OrderRequest;
import com.taller.bookstore.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse create(OrderRequest request, String userEmail);
    List<OrderResponse> findAll();
    List<OrderResponse> findMyOrders(String userEmail);
    OrderResponse findById(Long id, String userEmail);
    OrderResponse cancel(Long id, String userEmail);
}
