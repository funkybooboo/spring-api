package com.funkybooboo.store.products.mappers;

import com.funkybooboo.store.products.dtos.requests.CreateProductRequestDto;
import com.funkybooboo.store.products.entities.Product;
import com.funkybooboo.store.products.dtos.responses.ProductResponseDto;
import com.funkybooboo.store.products.dtos.requests.UpdateProductRequestDto;
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
