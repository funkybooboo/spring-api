package com.funkybooboo.store.services;

import com.funkybooboo.store.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.dtos.responses.CheckoutResponseDto;
import com.funkybooboo.store.entities.Order;
import com.funkybooboo.store.exceptions.CartNotFoundException;
import com.funkybooboo.store.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.repositories.CartRepository;
import com.funkybooboo.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    
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

        cartService.clearCart(cart.getId());

        return new CheckoutResponseDto(order.getId());
    }
}
