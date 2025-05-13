package com.funkybooboo.store.orders.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderItemResponseDto {
    private OrderProductResponseDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
