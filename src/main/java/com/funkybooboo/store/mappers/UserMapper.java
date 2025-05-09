package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.UserDto;
import com.funkybooboo.store.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
