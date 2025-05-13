package com.funkybooboo.store.payments.contollers;

import com.funkybooboo.store.shared.exceptions.ErrorResponseDto;
import com.funkybooboo.store.carts.exceptions.EmptyCartAtCheckoutException;
import com.funkybooboo.store.payments.dtos.requests.CheckoutRequestDto;
import com.funkybooboo.store.payments.dtos.requests.WebhookRequestDto;
import com.funkybooboo.store.payments.dtos.responses.CheckoutResponseDto;
import com.funkybooboo.store.payments.exceptions.PaymentGatewayException;
import com.funkybooboo.store.payments.services.CheckoutService;
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
