package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.GeoshareUser;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface GeoshareUserRepository extends CrudRepository<GeoshareUser, Long> {
	
	public GeoshareUser findByUsername(String username);
	
	default GeoshareUser findByIDOrThrow(Long id) {
		return findById(id).orElseThrow( () -> 
				new EntityNotFoundException("User not found in database."));
	}
	
}
