package com.funkybooboo.store.orders.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
    private BigDecimal totalPrice;
}
