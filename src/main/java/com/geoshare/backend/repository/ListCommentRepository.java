package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.ListComment;

@Repository
public interface ListCommentRepository extends CrudRepository<ListComment, Long> {

}
