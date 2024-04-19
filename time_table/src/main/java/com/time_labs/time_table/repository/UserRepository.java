package com.time_labs.time_table.repository;

import com.time_labs.time_table.repository.dao.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{ 'user_id' : ?0 }")
    User findByUserId(String userId);

    @Query(value = "{ 'user_id' : ?0 }", delete = true)
    void deleteByUserId(String userId);

    @Query("{ 'group_id' : ?0 }")
    List<User> findByGroupId(String groupId);
}
