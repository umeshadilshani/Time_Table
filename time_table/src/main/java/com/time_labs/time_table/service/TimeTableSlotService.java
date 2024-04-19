package com.time_labs.time_table.service;

import com.time_labs.time_table.repository.TimeTableSlotRepository;
import com.time_labs.time_table.repository.dao.TimeTableSlot;
import com.time_labs.time_table.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class TimeTableSlotService {

    @Autowired
    private TimeTableSlotRepository timeTableSlotRepository;

    @Autowired
    private NotificationService notificationService;

    public List<TimeTableSlot> getSlotsByGroupIdWeekAndDay(String groupId, int weekNo, DayOfWeek day) {
        return timeTableSlotRepository.findByGroupIdAndWeekNoAndDay(groupId, weekNo, day);
    }

    public boolean checkSlotOverlapForGroup(TimeTableSlot newSlot) {
        List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                .findByGroupIdAndWeekNoAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        newSlot.getGroupId(), newSlot.getWeekNo(), newSlot.getDay(),
                        newSlot.getEndTime(), newSlot.getStartTime());

        return overlappingSlots.isEmpty();
    }

    public boolean checkSlotOverlapForGroupExceptSameId(String id, TimeTableSlot newSlot) {
        List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                .findByGroupIdAndWeekNoAndDayAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                        newSlot.getGroupId(), newSlot.getWeekNo(), newSlot.getDay(),
                        newSlot.getEndTime(), newSlot.getStartTime(), id);

        return overlappingSlots.isEmpty();
    }

    public boolean checkSlotOverlapForLecturer(TimeTableSlot newSlot) {
        List<User> lecturers = newSlot.getLecturers();

        for (User lecturer : lecturers) {
            List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                    .findByWeekNoAndDayAndLecturersInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                            newSlot.getWeekNo(), newSlot.getDay(), List.of(lecturer),
                            newSlot.getEndTime(), newSlot.getStartTime());

            if (!overlappingSlots.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSlotOverlapForLecturerSameId(String id, TimeTableSlot newSlot) {
        List<User> lecturers = newSlot.getLecturers();

        for (User lecturer : lecturers) {
            List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                    .findByWeekNoAndDayAndLecturersInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                            newSlot.getWeekNo(), newSlot.getDay(), List.of(lecturer),
                            newSlot.getEndTime(), newSlot.getStartTime(), id);

            if (!overlappingSlots.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSlotOverlapForLocation(TimeTableSlot newSlot) {
        List<String > locations = newSlot.getLocations();

        for (String location : locations) {
            List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                    .findByWeekNoAndDayAndLocationsInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                            newSlot.getWeekNo(), newSlot.getDay(), List.of(location),
                            newSlot.getEndTime(), newSlot.getStartTime());

            if (!overlappingSlots.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public boolean checkSlotOverlapForLocationSameId(String id, TimeTableSlot newSlot) {
        List<String > locations = newSlot.getLocations();

        for (String location : locations) {
            List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                    .findByWeekNoAndDayAndLocationsInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                            newSlot.getWeekNo(), newSlot.getDay(), List.of(location),
                            newSlot.getEndTime(), newSlot.getStartTime(), id);

            if (!overlappingSlots.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSlotOverlapForResource(TimeTableSlot newSlot) {
        List<String > resources = newSlot.getResources();

        for (String resource : resources) {
            List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                    .findByWeekNoAndDayAndResourcesInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                            newSlot.getWeekNo(), newSlot.getDay(), List.of(resource),
                            newSlot.getEndTime(), newSlot.getStartTime());

            if (!overlappingSlots.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkSlotOverlapForResourceSameId(String id, TimeTableSlot newSlot) {
        List<String > resources = newSlot.getResources();

        for (String resource : resources) {
            List<TimeTableSlot> overlappingSlots = timeTableSlotRepository
                    .findByWeekNoAndDayAndResourcesInAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                            newSlot.getWeekNo(), newSlot.getDay(), List.of(resource),
                            newSlot.getEndTime(), newSlot.getStartTime(), id);

            if (!overlappingSlots.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public TimeTableSlot saveTimeTableSlot(TimeTableSlot timeTableSlot) {
        return timeTableSlotRepository.save(timeTableSlot);
    }

    public String updateTimeTableSlot(String id, TimeTableSlot timeTableSlot) {
        Optional<TimeTableSlot> foundSlot = timeTableSlotRepository.findById(id);

        if(foundSlot.isPresent()){
            TimeTableSlot updateSlot = foundSlot.get();
            updateSlot.setGroupId(timeTableSlot.getGroupId());
            updateSlot.setWeekNo(timeTableSlot.getWeekNo());
            updateSlot.setDay(timeTableSlot.getDay());
            updateSlot.setStartTime(timeTableSlot.getStartTime());
            updateSlot.setEndTime(timeTableSlot.getEndTime());
            updateSlot.setCourse(timeTableSlot.getCourse());
            updateSlot.setType(timeTableSlot.getType());
            updateSlot.setLecturers(timeTableSlot.getLecturers());
            updateSlot.setLocations(timeTableSlot.getLocations());
            updateSlot.setResources(timeTableSlot.getResources());

            timeTableSlotRepository.save(updateSlot);

            String message = notificationService.sendChangeAlert(timeTableSlot.getGroupId(), timeTableSlot.getLecturers());

            return message;
        }
        return "Could not update";
    }
}
