package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.responses.CartResponseDto;
import com.funkybooboo.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponseDto toResponseDto(Cart cart);
}
