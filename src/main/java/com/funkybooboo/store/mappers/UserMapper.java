package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.request.RegisterUserRequestDto;
import com.funkybooboo.store.dtos.request.UpdateUserRequestDto;
import com.funkybooboo.store.dtos.response.UserResponseDto;
import com.funkybooboo.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
    User toEntity(RegisterUserRequestDto userRequestDto);
    void update(UpdateUserRequestDto userRequestDto, @MappingTarget User user);
}
