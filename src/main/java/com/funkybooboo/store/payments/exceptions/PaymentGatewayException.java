package com.funkybooboo.store.payments.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentGatewayException extends RuntimeException {
    public PaymentGatewayException(String message) {
        super(message);
    }
}
