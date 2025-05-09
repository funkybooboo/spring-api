package com.funkybooboo.store.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
}
