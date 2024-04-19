package com.time_labs.time_table.repository;

import com.time_labs.time_table.repository.dao.TimeTableSlot;
import com.time_labs.time_table.repository.dao.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface TimeTableSlotRepository extends MongoRepository<TimeTableSlot, String> {

    List<TimeTableSlot> findByGroupIdAndWeekNoAndDay(String groupId, int weekNo, DayOfWeek day);

    List<TimeTableSlot> findByGroupIdAndWeekNoAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            String groupId, int weekNo, DayOfWeek day, LocalTime endTime, LocalTime startTime);

    List<TimeTableSlot> findByGroupIdAndWeekNoAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
            String groupId, int weekNo, DayOfWeek day, LocalTime endTime, LocalTime startTime, String id);
    
    List<TimeTableSlot> findByWeekNoAndDayAndLecturersInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            int weekNo, DayOfWeek day, List<User> lecturers, LocalTime endTime, LocalTime startTime);

    List<TimeTableSlot> findByWeekNoAndDayAndLocationsInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            int weekNo, DayOfWeek day, List<String> locations, LocalTime endTime, LocalTime startTime);

    List<TimeTableSlot> findByWeekNoAndDayAndResourcesInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            int weekNo, DayOfWeek day, List<String> resources, LocalTime endTime, LocalTime startTime);

    List<TimeTableSlot> findByWeekNoAndDayAndLecturersInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(int weekNo, DayOfWeek day, List<User> lecturer, LocalTime endTime, LocalTime startTime, String id);

    List<TimeTableSlot> findByWeekNoAndDayAndLocationsInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(int weekNo, DayOfWeek day, List<String> location, LocalTime endTime, LocalTime startTime, String id);

    List<TimeTableSlot> findByWeekNoAndDayAndResourcesInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(int weekNo, DayOfWeek day, List<String> resource, LocalTime endTime, LocalTime startTime, String id);
}
