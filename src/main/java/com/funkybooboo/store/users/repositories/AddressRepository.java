package com.funkybooboo.store.users.repositories;

import com.funkybooboo.store.users.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
