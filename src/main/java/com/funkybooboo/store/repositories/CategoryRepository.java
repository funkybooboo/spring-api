package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
