package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.responses.ErrorResponseDto;
import com.funkybooboo.store.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.dtos.responses.CheckoutResponseDto;
import com.funkybooboo.store.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    
    @PostMapping
    public CheckoutResponseDto checkout(
        @Valid @RequestBody CheckoutRequestDto requestDto
    ) {
        return checkoutService.checkout(requestDto);
    }

    @ExceptionHandler(EmptyCartAtCheckoutException.class)
    public ResponseEntity<ErrorResponseDto> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto("Empty cart at checkout"));
    }
}
