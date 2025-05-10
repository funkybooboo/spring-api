package com.funkybooboo.store.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequestDto {
    @NotNull
    private Long productId;
}
