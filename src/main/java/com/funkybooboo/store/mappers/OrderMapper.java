package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.responses.OrderResponseDto;
import com.funkybooboo.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDto toResponseDto(Order order);
}
