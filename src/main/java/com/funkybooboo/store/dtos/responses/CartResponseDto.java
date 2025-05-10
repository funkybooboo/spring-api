package com.funkybooboo.store.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CartResponseDto {
    private UUID id;
    private List<CartItemResponseDto> items = new ArrayList<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
