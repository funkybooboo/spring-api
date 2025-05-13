package com.funkybooboo.store.services.utils.paymentGateways;

import com.funkybooboo.store.dtos.requests.WebhookRequestDto;
import com.funkybooboo.store.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
    Optional<PaymentResult> parseWebhookRequestDto(WebhookRequestDto requestDto);
}
