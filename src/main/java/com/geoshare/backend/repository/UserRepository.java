package com.geoshare.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.geoshare.backend.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
