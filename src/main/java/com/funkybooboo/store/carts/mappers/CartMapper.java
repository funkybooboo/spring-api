package com.funkybooboo.store.carts.mappers;

import com.funkybooboo.store.carts.entites.CartItem;
import com.funkybooboo.store.carts.dtos.responses.CartItemResponseDto;
import com.funkybooboo.store.carts.dtos.responses.CartResponseDto;
import com.funkybooboo.store.carts.entites.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartResponseDto toResponseDto(Cart cart);
    
    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemResponseDto toResponseDto(CartItem cartItem);
}
