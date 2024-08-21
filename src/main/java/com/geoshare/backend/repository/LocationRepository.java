package com.geoshare.backend.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geoshare.backend.entity.Location;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {
	
	@Query("SELECT L FROM Location L WHERE L.user.id = :userID")
	List<Location> findAllByUser(Long userID);
	
	@Query("SELECT L FROM Location L JOIN GeoshareUser U ON L.user.id = U.id "
			+ "WHERE U.username = :username")
	List<Location> findAllByUser(String username);
	
	@Query("SELECT L FROM Location L WHERE L.country.id = :countryID")
	List<Location> findAllByCountry(Long countryID);
	
	@Query("SELECT L FROM Location L JOIN Country C ON L.country.id = C.id "
			+ "WHERE C.name = :countryName")
	List<Location> findAllByCountry(String countryName);
	
	default Location findByIDOrThrow(Long id) {
		return findById(id).orElseThrow( () ->
			new EntityNotFoundException("Location indicated by id: " + id + " was not found in database."));
	}
	
	@Query("SELECT L FROM Location L JOIN GeoshareUser U ON L.user.id = U.id "
			+ "WHERE U.username = :username AND L.listed = 0")
	List<Location> findUnlistedByUser(String username);
	
	@Query("SELECT L FROM Location L WHERE L.id IN :ids")
	List<Location> findAllById(Collection<Long> ids);
}











