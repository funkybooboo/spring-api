package com.funkybooboo.store.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
}
