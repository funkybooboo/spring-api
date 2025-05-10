package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.responses.CartResponseDto;
import com.funkybooboo.store.entities.Cart;
import com.funkybooboo.store.mappers.CartMapper;
import com.funkybooboo.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(
        UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);
        
        var cartResponseDto = cartMapper.toResponseDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartResponseDto.getId()).toUri();
        
        return ResponseEntity.created(uri).body(cartResponseDto);
    }
}
