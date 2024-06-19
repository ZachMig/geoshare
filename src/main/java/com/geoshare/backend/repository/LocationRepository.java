package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.Location;

@Repository
public interface LocationRepository extends CrudRepository<Location, Long> {

}
