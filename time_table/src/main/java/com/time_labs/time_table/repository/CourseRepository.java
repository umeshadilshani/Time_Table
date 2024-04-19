package com.time_labs.time_table.repository;

import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.repository.dao.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CourseRepository extends MongoRepository<Course, String> {

    @Query("{ 'code' : ?0 }")
    Course findByCode( String code);

    @Query(value = "{ 'code' : ?0 }", delete = true)
    void deleteByCourseCode(String userId);
}
