package com.funkybooboo.store.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Quantity must be greater than zero")
    @Max(value = 1000, message = "Quantity must be less than or equal to 100")
    private Integer quantity;
}
