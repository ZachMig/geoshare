package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.GeoshareUser;

@Repository
public interface GeoshareUserRepository extends CrudRepository<GeoshareUser, Long> {
	
	public GeoshareUser findByUsername(String username);
	
}
