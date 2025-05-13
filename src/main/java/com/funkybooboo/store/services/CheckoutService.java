package com.funkybooboo.store.services;

import com.funkybooboo.store.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.dtos.responses.CheckoutResponseDto;
import com.funkybooboo.store.entities.Order;
import com.funkybooboo.store.exceptions.CartNotFoundException;
import com.funkybooboo.store.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.repositories.CartRepository;
import com.funkybooboo.store.repositories.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    @Value("${websiteUrl}")
    private String websiteUrl;
    
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    
    public CheckoutResponseDto checkout(CheckoutRequestDto requestDto) throws StripeException {
        var cart = cartRepository.getCartWithItems(requestDto.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new EmptyCartAtCheckoutException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);
        
        // Create a checkout session
        var builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
            .setCancelUrl(websiteUrl + "/checkout-cancel");
        
        order.getItems().forEach(item -> {
            var lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmountDecimal(item.getUnitPrice())
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(item.getProduct().getName())
                                .build()
                        ).build()
                ).build();
            builder.addLineItem(lineItem);
        });
        
        var session = Session.create(builder.build());

        cartService.clearCart(cart.getId());

        return new CheckoutResponseDto(order.getId(), session.getUrl());
    }
}
