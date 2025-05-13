package com.funkybooboo.store.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secretKey}")
    private String secretKey;
    
    public void init() {
        Stripe.apiKey = secretKey;
    }
}
