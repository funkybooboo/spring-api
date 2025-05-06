package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
