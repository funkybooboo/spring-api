package com.funkybooboo.store.controllers;

import com.funkybooboo.store.dtos.ProductDto;
import com.funkybooboo.store.entities.Product;
import com.funkybooboo.store.mappers.ProductMapper;
import com.funkybooboo.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping("")
    public Iterable<ProductDto> getAllProducts(
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
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
