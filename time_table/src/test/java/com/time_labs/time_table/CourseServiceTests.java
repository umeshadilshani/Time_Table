package com.time_labs.time_table;

import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.repository.CourseRepository;
import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.service.CourseService;
import com.time_labs.time_table.web.dto.CourseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseServiceTests {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCourse() {
        Course course1 = new Course("CS101", "Computer Science", "Introduction to Computer Science", 3);
        Course course2 = new Course("CS102", "Computer Science", "Advanced Computer Science", 3);
        List<Course> courseList = Arrays.asList(course1, course2);
        when(courseRepository.findAll()).thenReturn(courseList);

        ResponseEntity<Object> responseEntity = courseService.getAllCourse();

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
        assertEquals(Constants.MESSAGE_FOUND, response.get("status"));
        assertEquals(2, ((List<CourseDto>) response.get("data")).size());
    }

    @Test
    void testGetCourseByCode_NotExists() {
        when(courseRepository.findByCode("CS101")).thenReturn(null);

        ResponseEntity<Object> responseEntity = courseService.getCourseByCode("CS101");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
        assertEquals(Constants.MESSAGE_NOT_FOUND, response.get("status"));
        assertEquals("", response.get("data"));
    }

    @Test
    void testUpdateByCourseCode_NotExists() {
        Course updatedCourse = new Course("CS101", "Updated Course", "Updated Description", 4);
        when(courseRepository.findByCode("CS101")).thenReturn(null);

        ResponseEntity<Object> responseEntity = courseService.updateByCourseCode("CS101", updatedCourse);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
        assertEquals(Constants.MESSAGE_NOT_FOUND, response.get("status"));
        assertEquals("", response.get("data"));
    }

    @Test
    void testDeleteByCourseCode_Exists() {
        Course course = new Course("CS101", "Computer Science", "Introduction to Computer Science", 3);
        when(courseRepository.findByCode("CS101")).thenReturn(course);
        doNothing().when(courseRepository).deleteByCourseCode("CS101");

        ResponseEntity<Object> responseEntity = courseService.deleteByCourseCode("CS101");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
        assertEquals(Constants.MESSAGE_DELETED_SUCCESSFULLY, response.get("status"));
        assertEquals("", response.get("data"));
    }

    @Test
    void testDeleteByCourseCode_NotExists() {
        when(courseRepository.findByCode("CS101")).thenReturn(null);

        ResponseEntity<Object> responseEntity = courseService.deleteByCourseCode("CS101");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> response = (Map<String, Object>) responseEntity.getBody();
        assertEquals(Constants.MESSAGE_NOT_FOUND, response.get("status"));
        assertEquals("", response.get("data"));
    }
}

