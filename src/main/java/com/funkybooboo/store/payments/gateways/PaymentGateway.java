package com.funkybooboo.store.payments.gateways;

import com.funkybooboo.store.orders.entities.Order;
import com.funkybooboo.store.payments.dtos.requests.WebhookRequestDto;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequestDto(WebhookRequestDto requestDto);
}
