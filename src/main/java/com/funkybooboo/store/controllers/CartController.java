package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.AddItemToCartRequestDto;
import com.funkybooboo.store.dtos.requests.UpdateCartItemRequestDto;
import com.funkybooboo.store.dtos.responses.CartItemResponseDto;
import com.funkybooboo.store.dtos.responses.CartResponseDto;
import com.funkybooboo.store.exceptions.CartNotFoundException;
import com.funkybooboo.store.exceptions.ProductNotFoundException;
import com.funkybooboo.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponseDto> createCart(
        UriComponentsBuilder uriBuilder
    ) {
        var cartResponseDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartResponseDto.getId()).toUri();
        return ResponseEntity.created(uri).body(cartResponseDto);
    }
    
    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adds a product to the cart")
    public ResponseEntity<CartItemResponseDto> addToCart(
        @PathVariable UUID cartId,
        @RequestBody AddItemToCartRequestDto requestDto,
        UriComponentsBuilder uriBuilder
    ) {
        
        var responseDto = cartService.addToCart(cartId, requestDto.getProductId());
        var uri = uriBuilder
                .path("/carts/{cartId}/items/{itemId}")
                .buildAndExpand(cartId, responseDto.getProduct().getId())
                .toUri();
        return ResponseEntity.created(uri).body(responseDto);
    }
    
    @GetMapping("/{cartId}")
    public CartResponseDto getCart(
        @PathVariable UUID cartId
    ) {
        return cartService.getCart(cartId);
    }
    
    @PutMapping("/{cartId}/items/{productId}")
    public CartItemResponseDto updateItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId,
        @Valid @RequestBody UpdateCartItemRequestDto requestDto
    ) {
        return cartService.updateItem(cartId, productId, requestDto.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
        @PathVariable("cartId") UUID cartId,
        @PathVariable("productId") Long productId
    ) {
        cartService.removeItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in cart"));
    }
}
