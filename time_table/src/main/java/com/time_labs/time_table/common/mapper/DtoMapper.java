package com.time_labs.time_table.common.mapper;

import com.time_labs.time_table.repository.dao.Course;
import com.time_labs.time_table.repository.dao.User;
import com.time_labs.time_table.web.dto.CourseDto;
import com.time_labs.time_table.web.dto.UserDto;

public class DtoMapper
{
    public static UserDto mapToUserDto( User user) {
        UserDto userDto = new UserDto();
        userDto.setUser_id(user.getUser_id());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static CourseDto mapToCourseDto( Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCode( course.getCode() );
        courseDto.setName( course.getName() );
        courseDto.setDescription( course.getDescription() );
        courseDto.setCredits( course.getCredits() );
        return courseDto;
    }
}
