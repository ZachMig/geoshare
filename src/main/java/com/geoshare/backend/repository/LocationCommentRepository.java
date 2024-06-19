package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.LocationComment;

@Repository
public interface LocationCommentRepository extends CrudRepository<LocationComment, Long> {

}
