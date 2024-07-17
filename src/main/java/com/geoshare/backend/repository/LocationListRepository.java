package com.geoshare.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.LocationList;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface LocationListRepository extends CrudRepository<LocationList, Long> {
	


	@Query("SELECT L FROM LocationList L WHERE L.user.id = :userID")
	List<LocationList> findAllByUser(Long userID);
	
	@Query("SELECT L FROM LocationList L JOIN GeoshareUser U ON L.user.id = U.id "
			+ "WHERE U.username = :username")
	List<LocationList> findAllByUser(String username);
	
    @Query("SELECT LIST FROM LocationList LIST JOIN LIST.locations LOCATION "
    		+ "WHERE LOCATION.id = :locationID")
	List<LocationList> findAllByLocationID(Long locationID);

	@Query("SELECT L FROM LocationList L WHERE L.id = :id")
	Optional<LocationList> findByID(Long id);
	
	default	LocationList findByIDOrThrow(Long id) {
		return findByID(id).orElseThrow(() -> 
		new EntityNotFoundException("List not found in the database."));
	}
	
	@Query("SELECT L FROM LocationList L WHERE L.name = :name")
	Optional <LocationList> findByName(String name);
	
	default LocationList findByNameOrThrow(String name) {
		return findByName(name).orElseThrow(() -> 
			new EntityNotFoundException("List not found in the database."));
	}
	
	@Query("SELECT L FROM LocationList L JOIN FETCH L.locations "
			+ "JOIN GeoshareUser U on L.user.username = :username")
	List<LocationList> findListsWithLocationsByUser(String username);
	
}
