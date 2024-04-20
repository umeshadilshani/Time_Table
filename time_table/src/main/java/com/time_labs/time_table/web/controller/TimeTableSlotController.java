package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.common.jwt.JwtFilter;
import com.time_labs.time_table.repository.dao.TimeTableSlot;
import com.time_labs.time_table.service.TimeTableSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/v1/timetable/slot")
public class TimeTableSlotController {

    @Autowired
    private TimeTableSlotService timeTableSlotService;

    @Autowired
    private JwtFilter jwtFilter;

    @PostMapping
    public ResponseEntity<Object> addTimeTableSlot(@RequestBody TimeTableSlot timeTableSlot) {
        try {
            if (jwtFilter.isAdmin()) {
                if (!timeTableSlotService.checkSlotOverlapForGroup(timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_GROUP);
                } else if (!timeTableSlotService.checkSlotOverlapForLecturer(timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_LECTURER);
                } else if (!timeTableSlotService.checkSlotOverlapForLocation(timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_LOCATION);
                } else if (!timeTableSlotService.checkSlotOverlapForResource(timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_RESOURCE);
                } else {
                    TimeTableSlot savedSlot = timeTableSlotService.saveTimeTableSlot(timeTableSlot);
                    return ResponseEntity.status(HttpStatus.CREATED).body(Constants.MESSAGE_SUCCESSFUL + " with ID: " + savedSlot.getId());
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.MESSAGE_UNAUTHORIZED_ACCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTimeTableSlot(@PathVariable String id, @RequestBody TimeTableSlot timeTableSlot) {
        try {
            if (jwtFilter.isAdmin()) {
                if (!timeTableSlotService.checkSlotOverlapForGroupExceptSameId(id, timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_GROUP);
                } else if (!timeTableSlotService.checkSlotOverlapForLecturerSameId(id, timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_LECTURER);
                } else if (!timeTableSlotService.checkSlotOverlapForLocationSameId(id, timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_LOCATION);
                } else if (!timeTableSlotService.checkSlotOverlapForResourceSameId(id, timeTableSlot)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.MESSAGE_SLOT_OVERLAP_RESOURCE);
                } else {
                    String message = timeTableSlotService.updateTimeTableSlot(id, timeTableSlot);
                    return ResponseEntity.status(HttpStatus.OK).body(message);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.MESSAGE_UNAUTHORIZED_ACCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/{weekNo}/{day}")
    public ResponseEntity<Object> getSlotsByGroupIdWeekAndDay(
            @PathVariable String groupId, @PathVariable int weekNo, @PathVariable DayOfWeek day) {
        try {
            List<TimeTableSlot> slots = timeTableSlotService.getSlotsByGroupIdWeekAndDay(groupId, weekNo, day);
            if (slots.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.MESSAGE_NO_RECORDS_FOUND);
            }
            return ResponseEntity.status(HttpStatus.OK).body(slots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{groupId}/{weekNo}")
    public ResponseEntity<Object> getSlotsByGroupIdAndWeek(
            @PathVariable String groupId, @PathVariable int weekNo) {
        try {
            List<TimeTableSlot> slots = timeTableSlotService.getSlotsByGroupIdAndWeek(groupId, weekNo);
            if (slots.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.MESSAGE_NO_RECORDS_FOUND);
            }
            return ResponseEntity.status(HttpStatus.OK).body(slots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSlotById(@PathVariable String id) {
        try {
            if (jwtFilter.isAdmin()) {
                String message = timeTableSlotService.deleteSlotById(id);
                if (message.equals(Constants.MESSAGE_DELETED_SUCCESSFULLY)) {
                    return ResponseEntity.status(HttpStatus.OK).body(message);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Constants.MESSAGE_UNAUTHORIZED_ACCESS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("lecturer/{userId}/{weekNo}/{day}")
    public List<TimeTableSlot> getSlotByUserIdWeekAndDay(@PathVariable String userId,@PathVariable int weekNo,@PathVariable DayOfWeek day) {
        try {
            return timeTableSlotService.findByLecturers_UserIdAndWeekNoAndDay(userId, weekNo, day);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching slots by lecturer, week, and day", e);
        }
    }

    @GetMapping("lecturer/{userId}/{weekNo}")
    public List<TimeTableSlot> getSlotByUserIdAndWeek(@PathVariable String userId,@PathVariable int weekNo) {
        try {
            return timeTableSlotService.findByLecturers_UserIdAndWeekNo(userId, weekNo);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching slots by lecturer, week, and day", e);
        }
    }
}
