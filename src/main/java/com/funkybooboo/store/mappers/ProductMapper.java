package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.ProductDto;
import com.funkybooboo.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product user);
}
