package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.requests.CreateProductRequestDto;
import com.funkybooboo.store.dtos.requests.UpdateProductRequestDto;
import com.funkybooboo.store.dtos.responses.ProductResponseDto;
import com.funkybooboo.store.entities.Product;
import com.funkybooboo.store.mappers.ProductMapper;
import com.funkybooboo.store.repositories.CategoryRepository;
import com.funkybooboo.store.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Tag(name = "Products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    public Iterable<ProductResponseDto> getAllProducts(
        @RequestParam(name = "sort", required = false, defaultValue = "") String sortBy, 
        @RequestParam(name = "categoryId", required = false) Byte categoryId
    ) {
        if (!Set.of("name", "description", "price").contains(sortBy)) {
            sortBy = "name";
        }
        Sort sort = Sort.by(sortBy);
        List<Product> products;
        if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, sort);
        } else {
            products = productRepository.findAll(sort);
        }
        return products.stream().map(productMapper::toResponseDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(
        @PathVariable Long id
    ) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }
    
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
        @RequestBody CreateProductRequestDto productRequestDto,
        UriComponentsBuilder uriBuilder
    ) {
        var category = categoryRepository.findById(productRequestDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        
        var product = productMapper.toEntity(productRequestDto);
        product.setCategory(category);
        productRepository.save(product);
        
        var productResponseDto = productMapper.toResponseDto(product);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productResponseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @PathVariable Long id,
        @RequestBody UpdateProductRequestDto productRequestDto
    ) {
        var category = categoryRepository.findById(productRequestDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productRequestDto, product);
        productRepository.save(product);

        return ResponseEntity.ok(productMapper.toResponseDto(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
        @PathVariable Long id
    ) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
