package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
