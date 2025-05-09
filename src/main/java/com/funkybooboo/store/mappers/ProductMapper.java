package com.funkybooboo.store.mappers;

import com.funkybooboo.store.dtos.requests.CreateProductRequestDto;
import com.funkybooboo.store.dtos.requests.UpdateProductRequestDto;
import com.funkybooboo.store.dtos.responses.ProductResponseDto;
import com.funkybooboo.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductResponseDto toResponseDto(Product user);
    
    Product toEntity(CreateProductRequestDto productRequestDto);
    
    @Mapping(target = "id", ignore = true)
    void update(UpdateProductRequestDto productRequestDto, @MappingTarget Product product);
}
