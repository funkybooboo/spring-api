package com.funkybooboo.store.repositories;

import com.funkybooboo.store.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
