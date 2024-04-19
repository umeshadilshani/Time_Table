package com.time_labs.time_table;
import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.repository.CourseRepository;
import com.time_labs.time_table.repository.UserRepository;
import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.repository.dao.User;
import com.time_labs.time_table.service.CourseEnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseEnrollmentServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseEnrollmentService courseEnrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCourseEnrollment_Success() {
        User user = new User("testUser", "Test User", "password", "student");
        List<String> courseList = new ArrayList<>();
        courseList.add("CS101");
        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("courseList", courseList);

        Course course = new Course("CS101", "Computer Science", "Introduction to Computer Science", 3);

        when(userRepository.findByUserId("testUser")).thenReturn(user);
        when(courseRepository.findByCode("CS101")).thenReturn(course);
        when(userRepository.save(any())).thenReturn(user);

        ResponseEntity<Object> responseEntity = courseEnrollmentService.courseEnrollment("testUser", requestBody);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.MESSAGE_UPDATED_SUCCESSFULLY, ((Map<String, Object>)responseEntity.getBody()).get("status"));
    }

    @Test
    void testCourseEnrollment_UserNotFound() {
        List<String> courseList = new ArrayList<>();
        courseList.add("CS101");
        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("courseList", courseList);

        when(userRepository.findByUserId("testUser")).thenReturn(null);

        ResponseEntity<Object> responseEntity = courseEnrollmentService.courseEnrollment("testUser", requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Constants.MESSAGE_UER_NOT_FOUND, ((Map<String, Object>)responseEntity.getBody()).get("status"));
    }

    @Test
    void testCourseUnEnrollment_UserNotFound() {
        List<String> courseList = new ArrayList<>();
        courseList.add("CS101");
        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("courseList", courseList);

        when(userRepository.findByUserId("testUser")).thenReturn(null);

        ResponseEntity<Object> responseEntity = courseEnrollmentService.courseUnEnrollment("testUser", requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Constants.MESSAGE_UER_NOT_FOUND, ((Map<String, Object>)responseEntity.getBody()).get("status"));
    }

    @Test
    void testGetEnrolledCourses_UserNotFound() {
        when(userRepository.findByUserId("testUser")).thenReturn(null);

        ResponseEntity<Object> responseEntity = courseEnrollmentService.getEnrolledCourses("testUser");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(Constants.MESSAGE_UER_NOT_FOUND, ((Map<String, Object>)responseEntity.getBody()).get("status"));
    }
}
