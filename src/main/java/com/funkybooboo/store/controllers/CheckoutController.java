package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.errors.ErrorDto;
import com.funkybooboo.store.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.dtos.responses.CheckoutResponseDto;
import com.funkybooboo.store.entities.Order;
import com.funkybooboo.store.entities.OrderItem;
import com.funkybooboo.store.entities.OrderStatus;
import com.funkybooboo.store.exceptions.CartNotFoundException;
import com.funkybooboo.store.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.repositories.CartRepository;
import com.funkybooboo.store.repositories.OrderRepository;
import com.funkybooboo.store.services.AuthService;
import com.funkybooboo.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CheckoutResponseDto> checkout(
        @Valid @RequestBody CheckoutRequestDto requestDto
    ) {
        var cart = cartRepository.getCartWithItems(requestDto.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }
        
        if (cart.getItems().isEmpty()) {
            throw new EmptyCartAtCheckoutException();
        }
        
        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());
        
        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });
        
        orderRepository.save(order);
        
        cartService.clearCart(cart.getId());
        
        return ResponseEntity.ok(new CheckoutResponseDto(order.getId()));
    }

    @ExceptionHandler(EmptyCartAtCheckoutException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("Empty cart at checkout"));
    }
}
