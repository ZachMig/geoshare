package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geoshare.backend.entity.ListComment;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface ListCommentRepository extends CrudRepository<ListComment, Long> {
	
	default ListComment findByIDOrThrow(Long id) {
		return findById(id).orElseThrow( () -> 
			new EntityNotFoundException("Unable to find comment in database."));
	}
	
	@Query("SELECT C FROM ListComment C WHERE C.user.id = :userID")
	List<ListComment> findAllByUser(Long userID);
	
	@Query("SELECT C FROM ListComment C JOIN GeoshareUser U ON C.user.id = U.id "
			+ "WHERE U.username = :username")
	List<ListComment> findAllByUser(String username);
	
	@Query("SELECT C FROM ListComment C WHERE C.locationList.id = :listID")
	List<ListComment> findAllByList(Long listID);
	
}
