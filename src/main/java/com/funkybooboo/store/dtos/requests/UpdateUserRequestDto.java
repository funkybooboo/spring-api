package com.funkybooboo.store.dtos.requests;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    public String name;
    public String email;
}
