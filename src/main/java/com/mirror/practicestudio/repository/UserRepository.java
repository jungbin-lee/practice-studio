package com.mirror.practicestudio.repository;

import com.mirror.practicestudio.domain.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User,String> {
   Optional<User> findByEmail(String email);
//   User findByEmail(String email);
}
