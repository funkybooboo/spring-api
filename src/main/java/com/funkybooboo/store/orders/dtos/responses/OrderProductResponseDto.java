package com.funkybooboo.store.orders.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderProductResponseDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
