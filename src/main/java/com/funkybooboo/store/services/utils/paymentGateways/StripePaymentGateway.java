package com.funkybooboo.store.services.utils.paymentGateways;

import com.funkybooboo.store.entities.Order;
import com.funkybooboo.store.entities.OrderItem;
import com.funkybooboo.store.exceptions.PaymentGatewayException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentGateway implements PaymentGateway {
    @Value("${websiteUrl}")
    private String websiteUrl;
    
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

    private Session createSession(Order order) throws StripeException {
        var builder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
            .setCancelUrl(websiteUrl + "/checkout-cancel");

        order.getItems().forEach(item -> {
            var lineItem = createLineItem(item);
            builder.addLineItem(lineItem);
        });

        return Session.create(builder.build());
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
