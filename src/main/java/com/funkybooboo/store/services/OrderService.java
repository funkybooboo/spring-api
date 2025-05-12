package com.funkybooboo.store.services;

import com.funkybooboo.store.dtos.responses.OrderResponseDto;
import com.funkybooboo.store.exceptions.OrderNotFoundException;
import com.funkybooboo.store.mappers.OrderMapper;
import com.funkybooboo.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    
    public List<OrderResponseDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toResponseDto).toList();
    }

    public OrderResponseDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFoundException::new);
        
        var user = authService.getCurrentUser();
        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("");
        }
        
        return orderMapper.toResponseDto(order);
    }
}
