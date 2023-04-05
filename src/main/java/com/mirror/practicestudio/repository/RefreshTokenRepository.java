package com.mirror.practicestudio.repository;

import com.mirror.practicestudio.domain.RefreshToken;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;
@EnableMongoRepositories
public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {
    Optional<RefreshToken> findByEmail(String email);


}
