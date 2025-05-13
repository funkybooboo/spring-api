package com.funkybooboo.store.products.repositories;

import com.funkybooboo.store.products.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
