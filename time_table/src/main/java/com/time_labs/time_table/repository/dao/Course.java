package com.time_labs.time_table.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    private String id;

    @Field("code")
    private String code;

    private String name;

    private String description;

    private Integer credits;

    public Course( String code, String name, String description, Integer credits )
    {
        this.code = code;
        this.name = name;
        this.description = description;
        this.credits = credits;
    }

    public Course get() {return this;}
}
