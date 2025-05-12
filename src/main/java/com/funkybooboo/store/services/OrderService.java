package com.funkybooboo.store.services;

import com.funkybooboo.store.dtos.responses.OrderResponseDto;
import com.funkybooboo.store.mappers.OrderMapper;
import com.funkybooboo.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
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
        var orders = orderRepository.getAllByCustomer(user);
        return orders.stream().map(orderMapper::toResponseDto).toList();
    }
}
