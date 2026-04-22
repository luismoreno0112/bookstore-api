package com.taller.bookstore.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponse {
    private Long id;
    private Long userId;
    private String userEmail;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}
