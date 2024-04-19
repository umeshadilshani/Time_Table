package com.time_labs.time_table.repository.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "timetable_slots")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeTableSlot {
    @Id
    private String id;

    private String groupId;

    private int weekNo;

    private String type;

    private DayOfWeek day;

    private LocalTime startTime;

    private LocalTime endTime;

    @DBRef
    private List<User> lecturers;

    @DBRef
    private Course course;

    private List<String> locations;

    private List<String> resources;

    public TimeTableSlot get(){
        return this;
    }
}
