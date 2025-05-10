package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.AddItemToCartRequestDto;
import com.funkybooboo.store.dtos.responses.CartItemResponseDto;
import com.funkybooboo.store.dtos.responses.CartResponseDto;
import com.funkybooboo.store.entities.Cart;
import com.funkybooboo.store.entities.CartItem;
import com.funkybooboo.store.mappers.CartMapper;
import com.funkybooboo.store.repositories.CartRepository;
import com.funkybooboo.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

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
    
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponseDto> addToCart(
        @PathVariable UUID cartId,
        @RequestBody AddItemToCartRequestDto requestDto,
        UriComponentsBuilder uriBuilder
    ) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        
        var product = productRepository.findById(requestDto.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        
        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
        
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItems().add(cartItem);
        }
        
        cartRepository.save(cart);
        
        var cartItemResponseDto = cartMapper.toResponseDto(cartItem);
        var uri = uriBuilder
                .path("/carts/{cartId}/items/{itemId}")
                .buildAndExpand(cart.getId(), cartItem.getId())
                .toUri();
        
        return ResponseEntity.created(uri).body(cartItemResponseDto);
    }
}
