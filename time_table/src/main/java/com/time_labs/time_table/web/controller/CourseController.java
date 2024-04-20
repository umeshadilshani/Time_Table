package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.common.jwt.JwtFilter;
import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.time_labs.time_table.common.Response.createResponse;

@RestController
@RequestMapping("v1/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private JwtFilter jwtFilter;

    @PostMapping
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        try {
            if (jwtFilter.isAdmin()){
                return courseService.createCourse(course);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllCourse() {
        try {
            return courseService.getAllCourse();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<Object> getCourseByCode(@PathVariable String code) {
        try {
            return courseService.getCourseByCode(code);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<Object> updateByCourseCode(@PathVariable String code, @RequestBody Course course) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isFaculty()) {
                return courseService.updateByCourseCode(code, course);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Object> deleteByCourseCode(@PathVariable String code) {
        try {
            if (jwtFilter.isAdmin()) {
                return courseService.deleteByCourseCode(code);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }
}
