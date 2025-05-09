package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.RegisterUserRequestDto;
import com.funkybooboo.store.dtos.UserResponseDto;
import com.funkybooboo.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
    User toEntity(RegisterUserRequestDto userDtoRequest);
}
