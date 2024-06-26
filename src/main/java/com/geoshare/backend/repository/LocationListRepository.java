package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.LocationList;

@Repository
public interface LocationListRepository extends CrudRepository<LocationList, Long> {
	
	@Query("SELECT L FROM LocationList L WHERE L.user.id = :userID")
	List<LocationList> findAllByUser(Long userID);
	
	@Query("SELECT L FROM LocationList L JOIN GeoshareUser U ON L.user.id = U.id "
			+ "WHERE U.username = :username")
	List<LocationList> findAllByUser(String username);
	
//	@Query("SELECT L FROM LocationList L ORDER BY L.likeCount DESC LIMIT :minLikes")
//	List<LocationList> findTopLiked(Long minLikes);
	
//	@Query("SELECT L FROM LocationList L WHERE L.name = :name")
//	LocationList findByName(String name);
	
	@Query("SELECT L FROM LocationList L WHERE L.id = :id")
	LocationList findByID(Long id);
	
}
