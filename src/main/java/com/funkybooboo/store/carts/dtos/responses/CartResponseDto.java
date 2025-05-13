package com.funkybooboo.store.carts.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartResponseDto {
    private UUID id;
    private List<CartItemResponseDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
