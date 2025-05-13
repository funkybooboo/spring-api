package com.funkybooboo.store.users.dtos.requests;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    public String name;
    public String email;
}
