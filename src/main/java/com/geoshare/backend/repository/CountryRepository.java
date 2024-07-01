package com.geoshare.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geoshare.backend.entity.Country;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
	
	@Query("SELECT C FROM Country C WHERE C.id = :id")
	Optional<Country> findByID(Long id);
	
	default Country findByIDOrThrow(long id) {
		return findByID(id).orElseThrow(() -> 
				new EntityNotFoundException("Country not found in database"));
	}
	
	List<Country> findAll();
	
}
