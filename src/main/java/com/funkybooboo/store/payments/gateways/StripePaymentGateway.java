package com.funkybooboo.store.payments.gateways;

import com.funkybooboo.store.orders.entities.Order;
import com.funkybooboo.store.orders.entities.OrderItem;
import com.funkybooboo.store.orders.entities.PaymentStatus;
import com.funkybooboo.store.payments.exceptions.PaymentGatewayException;
import com.funkybooboo.store.payments.dtos.requests.WebhookRequestDto;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;
    
    @Override
    public CheckoutSession createCheckoutSession(Order order) {
        try {
            var session = createSession(order);
            return new CheckoutSession(session.getUrl());
        }
        catch (StripeException ex) {
            System.out.println(ex.getMessage());
            throw new PaymentGatewayException();
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookRequestDto(WebhookRequestDto requestDto) {
        try {
            String payload   = requestDto.getPayload();
            String signature = requestDto.getHeaders().get("stripe-signature");

            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

            return switch (event.getType()) {
                case "payment_intent.succeeded" -> 
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
                case "payment_intent.payment_failed" -> 
                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
                default -> 
                    Optional.empty();
            };
        }
        catch (SignatureVerificationException ex) {
            throw new PaymentGatewayException("Invalid signature");
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event
            .getDataObjectDeserializer()
            .getObject()
            .orElseThrow(
                () -> new PaymentGatewayException("Could not deserialize Strip event. Check the SDK and API version.")
            );
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    private Session createSession(Order order) throws StripeException {
        var builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
            .setCancelUrl(websiteUrl + "/checkout-cancel")
            .setPaymentIntentData(createPaymentIntent(order));

        order.getItems().forEach(item -> {
            var lineItem = createLineItem(item);
            builder.addLineItem(lineItem);
        });

        return Session.create(builder.build());
    }

    private static SessionCreateParams.PaymentIntentData createPaymentIntent(Order order) {
        return SessionCreateParams.PaymentIntentData.builder().putMetadata("order_id", order.getId().toString()).build();
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(item.getQuantity()))
                .setPriceData(createPriceData(item)).build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("usd")
            .setUnitAmountDecimal(
                item.getUnitPrice().multiply(BigDecimal.valueOf(100))
            ).setProductData(createProductData(item)).build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
            .setName(item.getProduct().getName())
            .build();
    }
}
