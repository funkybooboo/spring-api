package com.funkybooboo.store.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequestDto {
    @NotNull(message = "Cart ID is required")
    private UUID cartId;
}
