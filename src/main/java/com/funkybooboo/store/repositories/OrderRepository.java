package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
