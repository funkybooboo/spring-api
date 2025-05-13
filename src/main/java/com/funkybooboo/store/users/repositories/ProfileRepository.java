package com.funkybooboo.store.users.repositories;

import com.funkybooboo.store.users.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
