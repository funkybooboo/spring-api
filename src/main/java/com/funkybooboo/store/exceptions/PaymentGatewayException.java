package com.funkybooboo.store.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentGatewayException extends RuntimeException {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
