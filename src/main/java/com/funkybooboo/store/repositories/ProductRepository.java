package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
