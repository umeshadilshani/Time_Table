package com.time_labs.time_table.web.controller;

import com.time_labs.time_table.common.Constants;
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

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUser() { return userService.getAllUser();}

    @GetMapping("/{user_id}")
    public ResponseEntity<Object> getUserById(@PathVariable String user_id) { return userService.getUserById(user_id);}

    @PutMapping("/{user_id}")
    public ResponseEntity<Object> updateUserById(@PathVariable String user_id, @RequestBody User user){ return userService.updateUserById(user_id, user);}

    @DeleteMapping("/{user_id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable String user_id){ return userService.deleteUserById(user_id);}

}


