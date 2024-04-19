package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.common.CommonUtils;
import com.time_labs.time_table.service.CourseEnrollmentService;
import com.time_labs.time_table.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/{user_id}")
public class CourseEnrollmentController
{
    @Autowired
    private CourseEnrollmentService courseEnrollmentService;

    @PutMapping("/enrol")
    public ResponseEntity<Object> courseEnrolment( @PathVariable String user_id, @RequestBody Map<String, List<String>> courseList)
    {
        return courseEnrollmentService.courseEnrollment(user_id, courseList);
    }

    @PutMapping("/un-enrol")
    public ResponseEntity<Object> courseUnEnrolment( @PathVariable String user_id, @RequestBody Map<String, List<String>> courseList)
    {
        return courseEnrollmentService.courseUnEnrollment(user_id, courseList);
    }

    @GetMapping
    public ResponseEntity<Object> getEnrolledCourses(@PathVariable String user_id){
        return courseEnrollmentService.getEnrolledCourses(user_id);
    }
}
