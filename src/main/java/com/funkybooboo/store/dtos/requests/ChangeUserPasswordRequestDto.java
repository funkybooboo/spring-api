package com.funkybooboo.store.dtos.requests;

import lombok.Data;

@Data
public class ChangeUserPasswordRequestDto {
    private String oldPassword;
    private String newPassword;
}
