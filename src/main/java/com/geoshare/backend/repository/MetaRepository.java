package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.Meta;

@Repository
public interface MetaRepository extends CrudRepository<Meta, Long> {
	
	List<Meta> findAll();
	
}
