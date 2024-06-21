package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geoshare.backend.entity.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
	
	@Query("SELECT C FROM Country C WHERE C.id = :id")
	Country findByID(Long id);
	
	List<Country> findAll();
	
}
