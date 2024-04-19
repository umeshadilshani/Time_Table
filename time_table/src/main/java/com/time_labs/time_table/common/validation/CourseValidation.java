package com.time_labs.time_table.common.validation;

import com.time_labs.time_table.common.CommonUtils;
import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.repository.dao.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.time_labs.time_table.common.Response.createResponse;

public class CourseValidation
{
    public static ResponseEntity<Object> validateCourse( Course course)
    {
        if ( CommonUtils.isEmpty(course.getCode()) || CommonUtils.isEmpty(course.getName()) || CommonUtils.isEmpty(course.getDescription())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_EMPTY_RECORD, "null"));
        }else if (course.getCredits() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_EMPTY_RECORD, "null"));
        }
        else if(!CommonUtils.isValidNumber( course.getCredits() )){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_INVALID_NUMBER, "null"));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
