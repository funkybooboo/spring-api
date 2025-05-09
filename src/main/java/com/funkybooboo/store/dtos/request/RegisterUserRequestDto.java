package com.funkybooboo.store.dtos.request;

import lombok.Data;

@Data
public class RegisterUserRequestDto {
    private String name;
    private String email;
    private String password;
}
