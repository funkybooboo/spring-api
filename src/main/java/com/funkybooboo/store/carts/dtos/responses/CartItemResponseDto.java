package com.funkybooboo.store.carts.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponseDto {
    private CartProductResponseDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
