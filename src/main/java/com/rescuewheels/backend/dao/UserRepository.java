package com.rescuewheels.backend.dao;

import com.rescuewheels.backend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{ 'email' : ?0 }")
    User findByEmail(String email);
}
