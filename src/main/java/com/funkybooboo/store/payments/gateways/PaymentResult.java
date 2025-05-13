package com.funkybooboo.store.payments.gateways;

import com.funkybooboo.store.orders.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
