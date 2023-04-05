package com.mirror.practicestudio.repository;


import com.mirror.practicestudio.domain.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


import java.util.List;

@EnableMongoRepositories
public interface VideoRepository extends MongoRepository<Video,String> {
    List<Video> findAllByOrderByDateDesc();
Page<Video> findByuserEmail(String email, Pageable pageable);
    Video findBy_id(String _id);//시간 역순정렬

}
