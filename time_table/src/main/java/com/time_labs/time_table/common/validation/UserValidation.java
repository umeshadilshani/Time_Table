package com.time_labs.time_table.common.validation;

import com.time_labs.time_table.common.CommonUtils;
import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.repository.dao.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.time_labs.time_table.common.Response.createResponse;

public class UserValidation {
    public static ResponseEntity<Object> validateUser(User user)
    {
        if ( CommonUtils.isEmpty(user.getUser_id()) || CommonUtils.isEmpty(user.getName()) || CommonUtils.isEmpty(user.getPassword()) || CommonUtils.isEmpty(user.getRole())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_EMPTY_RECORD, "null"));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
