package com.time_labs.time_table.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String id;

    @Field("user_id")
    private String user_id;

    private String name;

    private String password;

    private String role;

    @Field("group_id")
    private String groupId;

    @DBRef
    private List<Course> enrolled_courses;

    public User(String user_id, String name, String password, String role) {
        this.user_id = user_id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public User get() {
        return this;
    }

    public void addEnrolledCourse(Course course) {
        enrolled_courses.add(course);
    }

    public void removeEnrolledCourse(Course course) {
        enrolled_courses.remove(course);
    }
}

