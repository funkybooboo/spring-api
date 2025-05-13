package com.funkybooboo.store.orders.controllers;

import com.funkybooboo.store.global.exceptions.ErrorResponseDto;
import com.funkybooboo.store.orders.exceptions.OrderNotFoundException;
import com.funkybooboo.store.orders.dtos.responses.OrderResponseDto;
import com.funkybooboo.store.orders.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(
        @PathVariable("orderId") Long orderId
    ) {
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleOrderNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto("Order not found"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponseDto("Access denied"));
    }
}
