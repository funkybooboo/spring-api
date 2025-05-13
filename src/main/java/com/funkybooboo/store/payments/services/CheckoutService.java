package com.funkybooboo.store.payments.services;

import com.funkybooboo.store.orders.entities.Order;
import com.funkybooboo.store.carts.exceptions.CartNotFoundException;
import com.funkybooboo.store.carts.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.auth.services.AuthService;
import com.funkybooboo.store.carts.services.CartService;
import com.funkybooboo.store.carts.repositories.CartRepository;
import com.funkybooboo.store.orders.repositories.OrderRepository;
import com.funkybooboo.store.payments.gateways.PaymentGateway;
import com.funkybooboo.store.payments.exceptions.PaymentGatewayException;
import com.funkybooboo.store.payments.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.payments.dtos.requests.WebhookRequestDto;
import com.funkybooboo.store.payments.dtos.responses.CheckoutResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;
    
    @Transactional
    public CheckoutResponseDto checkout(CheckoutRequestDto requestDto) {
        var cart = cartRepository.getCartWithItems(requestDto.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new EmptyCartAtCheckoutException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);
        
        try {
            var session = paymentGateway.createCheckoutSession(order);

            cartService.clearCart(cart.getId());

            return new CheckoutResponseDto(order.getId(), session.getCheckoutUrl());
        } catch (PaymentGatewayException ex) {
            System.out.println(ex.getMessage());
            orderRepository.delete(order);
            throw ex;
        }
    }
    
    public void handleWebhookEvent(WebhookRequestDto requestDto) {
        paymentGateway
            .parseWebhookRequestDto(requestDto)
            .ifPresent(paymentResult -> {
                var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                order.setStatus(paymentResult.getPaymentStatus());
                orderRepository.save(order);
            });
    }
}
