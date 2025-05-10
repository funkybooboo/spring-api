package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.AddItemToCartRequestDto;
import com.funkybooboo.store.dtos.requests.UpdateCartItemRequestDto;
import com.funkybooboo.store.dtos.responses.CartItemResponseDto;
import com.funkybooboo.store.dtos.responses.CartResponseDto;
import com.funkybooboo.store.entities.Cart;
import com.funkybooboo.store.entities.CartItem;
import com.funkybooboo.store.mappers.CartMapper;
import com.funkybooboo.store.repositories.CartRepository;
import com.funkybooboo.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
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
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        
        var product = productRepository.findById(requestDto.getProductId()).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        
        var cartItem = cart.addItem(product);
        
        cartRepository.save(cart);
        
        var cartItemResponseDto = cartMapper.toResponseDto(cartItem);
        var uri = uriBuilder
                .path("/carts/{cartId}/items/{itemId}")
                .buildAndExpand(cart.getId(), cartItem.getId())
                .toUri();
        
        return ResponseEntity.created(uri).body(cartItemResponseDto);
    }
    
    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponseDto> getCart(
        @PathVariable UUID cartId
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(cartMapper.toResponseDto(cart));
    }
    
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequestDto requestDto
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
        }

        var cartItem = cart.getItem(productId);
        
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Product was not found in the cart"));
        }
        
        cartItem.setQuantity(requestDto.getQuantity());
        cartRepository.save(cart);
        
        return ResponseEntity.ok(cartMapper.toResponseDto(cartItem));
    }
}
