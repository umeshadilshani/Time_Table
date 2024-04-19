package com.time_labs.time_table.service;

import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.repository.CourseRepository;
import com.time_labs.time_table.repository.UserRepository;
import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.time_labs.time_table.common.Response.createResponse;

@Service
public class CourseEnrollmentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<Object> courseEnrollment(String user_id, Map<String, List<String>> courseList)
    {
        User foundUser = userRepository.findByUserId(user_id);
        List<String> courses = courseList.get("courseList");

        if (foundUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_UER_NOT_FOUND, null));
        } else {
            for (String code : courses) {
                Course course = courseRepository.findByCode(code);
                if (course != null) {
                    Course newCourse = new Course();
                    newCourse.setId(course.getId());
                    newCourse.setCode( course.getCode() );
                    newCourse.setName( course.getName() );
                    newCourse.setDescription( course.getDescription() );
                    newCourse.setCredits( course.getCredits() );
                    if(foundUser.getEnrolled_courses() == null){
                        foundUser.setEnrolled_courses(new ArrayList<>());
                    }
                    foundUser.addEnrolledCourse(newCourse);
                }
            }
            User enrolledUser = foundUser.get();
            enrolledUser.setUser_id( foundUser.getUser_id() );
            enrolledUser.setName( foundUser.getName() );
            enrolledUser.setPassword( foundUser.getPassword() );
            enrolledUser.setRole( foundUser.getRole() );
            enrolledUser.setEnrolled_courses( foundUser.getEnrolled_courses() );

            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_UPDATED_SUCCESSFULLY, userRepository.save(enrolledUser)));
        }
    }

    public ResponseEntity<Object> courseUnEnrollment(String user_id, Map<String, List<String>> courseList)
    {
        User foundUser = userRepository.findByUserId(user_id);
        List<String> courses = courseList.get("courseList");

        if (foundUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_UER_NOT_FOUND, null));
        } else {
            for (String code : courses) {
                Course course = courseRepository.findByCode(code);
                if (course != null) {
                    Course newCourse = new Course();
                    newCourse.setId(course.getId());
                    newCourse.setCode( course.getCode() );
                    newCourse.setName( course.getName() );
                    newCourse.setDescription( course.getDescription() );
                    newCourse.setCredits( course.getCredits() );
                    foundUser.removeEnrolledCourse(newCourse);
                }
            }
            User enrolledUser = foundUser.get();
            enrolledUser.setUser_id( foundUser.getUser_id() );
            enrolledUser.setName( foundUser.getName() );
            enrolledUser.setPassword( foundUser.getPassword() );
            enrolledUser.setRole( foundUser.getRole() );
            enrolledUser.setEnrolled_courses( foundUser.getEnrolled_courses() );
            userRepository.save(enrolledUser);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_UPDATED_SUCCESSFULLY, userRepository.save(enrolledUser)));
        }
    }

    public ResponseEntity<Object> getEnrolledCourses(String user_id) {
        User foundUser = userRepository.findByUserId(user_id);

        if (foundUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_UER_NOT_FOUND, null));
        } else {
//            List<String> enrolledCourseNames = foundUser.getEnrolled_courses().stream()
//                    .map(Course::getName)
//                    .collect(Collectors.toList());

//            List<Map<String, String>> enrolledCoursesInfo = foundUser.getEnrolled_courses().stream()
//                    .map(course -> {
//                        Map<String, String> courseInfo = new HashMap<>();
//                        courseInfo.put("name", course.getName());
//                        courseInfo.put("code", course.getCode());
//                        return courseInfo;
//                    })
//                    .collect(Collectors.toList());

            List<Course> enrolledCourses = foundUser.getEnrolled_courses();
            return ResponseEntity.status(HttpStatus.OK).body(createResponse("Enrolled Courses:", enrolledCourses));
        }
    }
}
