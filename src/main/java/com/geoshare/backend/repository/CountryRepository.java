package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

}
