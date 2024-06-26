package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.LocationComment;

@Repository
public interface LocationCommentRepository extends CrudRepository<LocationComment, Long> {
	
	@Query("SELECT C FROM LocationComment C WHERE C.location.id = :locationID")
	List<LocationComment> findAllByLocation(Long locationID);
	
	@Query("SELECT C FROM LocationComment C WHERE C.user.id = :userID")
	List<LocationComment> findAllByUser(Long userID);
	
	@Query("SELECT C FROM LocationComment C JOIN GeoshareUser U ON C.user.id = U.id "
			+ "WHERE U.username = :username")
	List<LocationComment> findAllByUser(String username);
	
	@Query("SELECT C FROM LocationComment C WHERE C.id = :id")
	LocationComment findByID(Long id);
	
}
