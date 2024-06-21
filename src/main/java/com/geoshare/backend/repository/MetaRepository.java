package com.geoshare.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.geoshare.backend.entity.Meta;

@Repository
public interface MetaRepository extends CrudRepository<Meta, Long> {
	
	@Query("SELECT M FROM Meta M WHERE M.id = :id")
	Meta findByID(Long id);
	
	List<Meta> findAll();
	
}
