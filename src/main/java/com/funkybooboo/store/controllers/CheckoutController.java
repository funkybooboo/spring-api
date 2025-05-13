package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.WebhookRequestDto;
import com.funkybooboo.store.dtos.responses.ErrorResponseDto;
import com.funkybooboo.store.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.dtos.responses.CheckoutResponseDto;
import com.funkybooboo.store.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.exceptions.PaymentGatewayException;
import com.funkybooboo.store.services.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
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
    
    @PostMapping("/webhook")
    public void webhook(
        @RequestHeader Map<String, String> headers,
        @RequestBody String payload
    ) {
        checkoutService.handleWebhookEvent(new WebhookRequestDto(headers, payload));
    }
    
    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<ErrorResponseDto> handlePaymentGatewayException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto("Error creating a checkout session"));
    }

    @ExceptionHandler(EmptyCartAtCheckoutException.class)
    public ResponseEntity<ErrorResponseDto> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto("Empty cart at checkout"));
    }
}
