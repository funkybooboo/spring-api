package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.requests.CreateUserRequestDto;
import com.funkybooboo.store.dtos.requests.UpdateUserRequestDto;
import com.funkybooboo.store.dtos.responses.UserResponseDto;
import com.funkybooboo.store.entities.User;
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
