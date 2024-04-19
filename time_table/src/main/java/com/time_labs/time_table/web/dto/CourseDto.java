package com.time_labs.time_table.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto
{
    private String code;

    private String name;

    private String description;

    private Integer credits;
}
