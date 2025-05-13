package com.funkybooboo.store.payments.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CheckoutResponseDto {
    private Long orderId;
    private String checkoutUrl;
}
