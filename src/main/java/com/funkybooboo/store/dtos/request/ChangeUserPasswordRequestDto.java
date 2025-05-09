package com.funkybooboo.store.dtos.request;

import lombok.Data;

@Data
public class ChangeUserPasswordRequestDto {
    private String oldPassword;
    private String newPassword;
}
