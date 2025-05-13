package com.funkybooboo.store.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class WebhookRequestDto {
    private Map<String, String> headers;
    private String payload;
}
