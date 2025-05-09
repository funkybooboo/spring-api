package com.funkybooboo.store.dtos.requests;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String name;
    private String email;
    private String password;
}
