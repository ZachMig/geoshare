package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.LocationList;

@Repository
public interface LocationListRepository extends CrudRepository<LocationList, Long> {

}
