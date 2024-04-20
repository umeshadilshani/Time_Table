package com.time_labs.time_table.service;

import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.common.jwt.JwtFilter;
import com.time_labs.time_table.common.mapper.DtoMapper;
import com.time_labs.time_table.common.validation.CourseValidation;
import com.time_labs.time_table.repository.CourseRepository;
import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.web.dto.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.time_labs.time_table.common.Response.createResponse;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JwtFilter jwtFilter;

    public ResponseEntity<Object> createCourse(Course course) {
        try {
            if (jwtFilter.isAdmin()) {
                if (getCourseByCode(course.getCode()).getStatusCode() == HttpStatus.FOUND) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_ALREADY_EXISTS, null));
                }
                if (CourseValidation.validateCourse(course).getStatusCode() == HttpStatus.OK) {
                    Course newCourse = new Course(course.getCode(), course.getName(), course.getDescription(), course.getCredits());
                    Course savedCourse = courseRepository.save(newCourse);
                    return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(Constants.MESSAGE_SUCCESSFUL, DtoMapper.mapToCourseDto(savedCourse)));
                }
                return CourseValidation.validateCourse(course);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getAllCourse() {
        try {
            List<CourseDto> courses = courseRepository.findAll()
                    .stream()
                    .map(DtoMapper::mapToCourseDto)
                    .toList();

            if (courses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_NO_RECORDS_FOUND, null));
            }

            return ResponseEntity.status(HttpStatus.FOUND).body(createResponse(Constants.MESSAGE_FOUND, courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getCourseByCode(String code) {
        try {
            Course course = courseRepository.findByCode(code);

            if (course != null) {
                return ResponseEntity.status(HttpStatus.FOUND).body(createResponse(Constants.MESSAGE_FOUND, DtoMapper.mapToCourseDto(course)));
            }
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> updateByCourseCode(String code, Course course) {
        try {
            Course foundCourse = courseRepository.findByCode(code);

            if (foundCourse != null) {
                Course updateCourse = foundCourse;
                updateCourse.setCode(code);
                updateCourse.setName(course.getName());
                updateCourse.setDescription(course.getDescription());
                updateCourse.setCredits(course.getCredits());

                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_UPDATED_SUCCESSFULLY, DtoMapper.mapToCourseDto(courseRepository.save(updateCourse))));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> deleteByCourseCode(String code) {
        try {
            ResponseEntity<Object> responseEntity = getCourseByCode(code);

            if (responseEntity.getStatusCode() == HttpStatus.FOUND) {
                courseRepository.deleteByCourseCode(code);
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_DELETED_SUCCESSFULLY, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }
}
