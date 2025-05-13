package com.funkybooboo.store.services.utils.paymentGateways;

import com.funkybooboo.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
