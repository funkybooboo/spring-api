package com.funkybooboo.store.orders.mappers;

import com.funkybooboo.store.orders.dtos.responses.OrderResponseDto;
import com.funkybooboo.store.orders.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDto toResponseDto(Order order);
}
