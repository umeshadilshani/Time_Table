package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.common.jwt.JwtFilter;
import com.time_labs.time_table.repository.dao.User;
import com.time_labs.time_table.service.UserService;
import com.time_labs.time_table.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.time_labs.time_table.common.Response.createResponse;

@RestController
@RequestMapping("v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtFilter jwtFilter;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        try {
            return userService.login(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        try {
            if (jwtFilter.isAdmin()){
                return userService.createUser(user);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser() {
        try {
            return userService.getAllUser();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUserById(@PathVariable String user_id) {
        try {
            return userService.getUserById(user_id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<Object> updateUserById(@PathVariable String user_id, @RequestBody User user) {
        try {
            if (jwtFilter.isAdmin() || jwtFilter.isFaculty()) {
                return userService.updateUserById(user_id, user);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable String user_id) {
        try {
            if (jwtFilter.isAdmin()) {
                return userService.deleteUserById(user_id);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(createResponse(Constants.MESSAGE_UNAUTHORIZED_ACCESS, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{user_id}/reset-password")
    public ResponseEntity<Object> resetPassword(@PathVariable String user_id, @RequestBody User user) {
        try {
            return userService.resetPassword(user_id, user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }
}
