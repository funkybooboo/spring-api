package com.funkybooboo.store.carts.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequestDto {
    @NotNull
    private Long productId;
}
