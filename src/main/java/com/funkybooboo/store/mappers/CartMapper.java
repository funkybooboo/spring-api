package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.responses.CartItemResponseDto;
import com.funkybooboo.store.dtos.responses.CartResponseDto;
import com.funkybooboo.store.entities.Cart;
import com.funkybooboo.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartResponseDto toResponseDto(Cart cart);
    
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemResponseDto toResponseDto(CartItem cartItem);
}
