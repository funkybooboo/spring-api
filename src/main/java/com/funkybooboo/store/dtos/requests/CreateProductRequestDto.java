package com.funkybooboo.store.dtos.requests;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Byte categoryId;
}
