package com.time_labs.time_table.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group
{
    @Id
    private String id;

    @Field("code")
    private String code;

    private Integer year;

    private Integer semester;

    private String batch;

    private String department;

    private Integer main_group;

    private Integer sub_group;

    public Group get() {return this;}
}
