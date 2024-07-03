package com.geoshare.backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.GeoshareUser;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface GeoshareUserRepository extends CrudRepository<GeoshareUser, Long> {
	
	default GeoshareUser findByIDOrThrow(Long id) {
		return findById(id).orElseThrow( () -> 
				new EntityNotFoundException("User not found in database."));
	}
	
	default GeoshareUser findByUsernameOrThrow(String username) {
		return findByUsername(username).orElseThrow( () -> 
				new EntityNotFoundException("User not found in database."));
	}

	Optional<GeoshareUser> findByUsername(String username);
	
}
