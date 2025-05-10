package com.funkybooboo.store.dtos.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
