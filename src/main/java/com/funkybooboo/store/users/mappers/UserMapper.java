package com.funkybooboo.store.users.mappers;

import com.funkybooboo.store.users.dtos.requests.CreateUserRequestDto;
import com.funkybooboo.store.users.dtos.requests.UpdateUserRequestDto;
import com.funkybooboo.store.users.dtos.responses.UserResponseDto;
import com.funkybooboo.store.users.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toResponseDto(User user);
    
    User toEntity(CreateUserRequestDto userRequestDto);
    
    @Mapping(target = "id", ignore = true)
    void update(UpdateUserRequestDto userRequestDto, @MappingTarget User user);
}
