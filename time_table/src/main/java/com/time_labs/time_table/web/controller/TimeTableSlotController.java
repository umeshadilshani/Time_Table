package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.repository.dao.TimeTableSlot;
import com.time_labs.time_table.service.TimeTableSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/v1/timetable/slot")
public class TimeTableSlotController {

    @Autowired
    private TimeTableSlotService timeTableSlotService;

    @PostMapping
    public String addTimeTableSlot(@RequestBody TimeTableSlot timeTableSlot) {
        if (!timeTableSlotService.checkSlotOverlapForGroup(timeTableSlot)) {
            return "Slot overlaps with existing slots for the group.";
        } else if(!timeTableSlotService.checkSlotOverlapForLecturer(timeTableSlot)) {
            return "Slot overlaps with existing slots for the lecturer.";
        }else if(!timeTableSlotService.checkSlotOverlapForLocation(timeTableSlot)) {
            return "Slot overlaps with existing slots for the location.";
        }else if(!timeTableSlotService.checkSlotOverlapForResource(timeTableSlot)) {
            return "Slot overlaps with existing slots for the resources.";
        }else {
            TimeTableSlot savedSlot = timeTableSlotService.saveTimeTableSlot(timeTableSlot);
            return "Slot added successfully with ID: " + savedSlot.getId();
        }
    }

    @PutMapping("/{id}")
    public String updateTimeTableSlot(@PathVariable String id, @RequestBody TimeTableSlot timeTableSlot) {
        if (!timeTableSlotService.checkSlotOverlapForGroupExceptSameId(id, timeTableSlot)) {
            return "Slot overlaps with existing slots for the group.";
        } else if(!timeTableSlotService.checkSlotOverlapForLecturerSameId(id, timeTableSlot)) {
            return "Slot overlaps with existing slots for the lecturer.";
        }else if(!timeTableSlotService.checkSlotOverlapForLocationSameId(id, timeTableSlot)) {
            return "Slot overlaps with existing slots for the location.";
        }else if(!timeTableSlotService.checkSlotOverlapForResourceSameId(id, timeTableSlot)) {
            return "Slot overlaps with existing slots for the resources.";
        }else {
            return timeTableSlotService.updateTimeTableSlot(id, timeTableSlot);
        }
    }

    @GetMapping("/{groupId}/{weekNo}/{day}")
    public List<TimeTableSlot> getSlotsByGroupIdWeekAndDay(
            @PathVariable String groupId, @PathVariable int weekNo, @PathVariable DayOfWeek day) {
        return timeTableSlotService.getSlotsByGroupIdWeekAndDay(groupId, weekNo, day);
    }
}
