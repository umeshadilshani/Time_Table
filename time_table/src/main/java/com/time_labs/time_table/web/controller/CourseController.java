package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.repository.dao.User;
import com.time_labs.time_table.service.CourseService;
import com.time_labs.time_table.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/course")
public class CourseController
{
    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @GetMapping
    public ResponseEntity<Object> getAllCourse() { return courseService.getAllCourse();}

    @GetMapping("/{code}")
    public ResponseEntity<Object> getCourseByCode(@PathVariable String code) { return courseService.getCourseByCode(code);}

    @PutMapping("/{code}")
    public ResponseEntity<Object> updateByCourseCode(@PathVariable String code, @RequestBody Course course){ return courseService.updateByCourseCode(code, course);}

    @DeleteMapping("/{code}")
    public ResponseEntity<Object> deleteByCourseCode(@PathVariable String code){ return courseService.deleteByCourseCode(code);}
}


