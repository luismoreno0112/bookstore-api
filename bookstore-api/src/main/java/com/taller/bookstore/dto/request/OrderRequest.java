package com.taller.bookstore.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "Los items del pedido son obligatorios")
    @Size(min = 1, message = "El pedido debe tener al menos un item")
    private List<OrderItemRequest> items;
}
