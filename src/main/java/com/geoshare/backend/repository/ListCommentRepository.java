package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.ListComment;

@Repository
public interface ListCommentRepository extends CrudRepository<ListComment, Long> {
	
	@Query("SELECT C FROM ListComment C WHERE C.locationList.id = :listID")
	List<ListComment> findAllByList(Long listID);
	
	@Query("SELECT C FROM ListComment C WHERE C.user.id = :userID")
	List<ListComment> findAllByUser(Long userID);
	
	@Query("SELECT C FROM ListComment C JOIN GeoshareUser U ON C.user.id = U.id "
			+ "WHERE U.username = :username")
	List<ListComment> findAllByUser(String username);
	
	@Query("SELECT C FROM ListComment C WHERE C.id = :id")
	ListComment findByID(Long id);
	
}
