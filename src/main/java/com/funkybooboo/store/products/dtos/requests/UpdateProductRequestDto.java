package com.funkybooboo.store.products.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
