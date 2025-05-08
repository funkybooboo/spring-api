package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
