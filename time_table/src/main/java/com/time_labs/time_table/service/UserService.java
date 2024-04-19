package com.time_labs.time_table.service;

import com.time_labs.time_table.common.CommonUtils;
import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.common.jwt.CustomerUserDetailsService;
import com.time_labs.time_table.common.jwt.JwtFilter;
import com.time_labs.time_table.common.jwt.JwtUtil;
import com.time_labs.time_table.common.mapper.DtoMapper;
import com.time_labs.time_table.common.validation.UserValidation;
import com.time_labs.time_table.repository.CourseRepository;
import com.time_labs.time_table.repository.UserRepository;
import com.time_labs.time_table.repository.dao.User;
import com.time_labs.time_table.web.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.time_labs.time_table.common.Response.createResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    public ResponseEntity<Object> createUser(User user) {
        User newUser = new User();
        if(getUserById(user.getUser_id()).getStatusCode() == HttpStatus.FOUND){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_ALREADY_EXISTS, null));
        }
        if (UserValidation.validateUser(user).getStatusCode() == HttpStatus.OK) {
            newUser.setUser_id(user.getUser_id());
            newUser.setName(user.getName());
            newUser.setPassword(user.getPassword());
            newUser.setRole(user.getRole());

            if (user.getGroupId() != null){
                newUser.setGroupId(user.getGroupId());
            }
            User savedUser = userRepository.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(Constants.MESSAGE_SUCCESSFUL, DtoMapper.mapToUserDto(savedUser)));
        }
        return UserValidation.validateUser(user);
    }

    public ResponseEntity<Object> getAllUser() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(DtoMapper::mapToUserDto)
                .toList();

        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_NO_RECORDS_FOUND, null));
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(createResponse(Constants.MESSAGE_FOUND, users));
    }

    public ResponseEntity<Object> getUserById(String user_id) {
        User user =  userRepository.findByUserId(user_id);

        if(user != null){
            return ResponseEntity.status(HttpStatus.FOUND).body(createResponse(Constants.MESSAGE_FOUND, DtoMapper.mapToUserDto(user)));
        }
        return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
    }

    public ResponseEntity<Object> updateUserById(String user_id, User user) {
        User foundUser = userRepository.findByUserId(user_id);

        if(foundUser != null){
            User updateUser = foundUser.get();
            updateUser.setUser_id(user_id);
            updateUser.setName(user.getName());
            updateUser.setPassword(user.getPassword());
            updateUser.setRole(user.getRole());

            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_UPDATED_SUCCESSFULLY, DtoMapper.mapToUserDto(userRepository.save(updateUser))));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
    }

    public ResponseEntity<Object> deleteUserById(String user_id) {
        ResponseEntity<Object> responseEntity = getUserById(user_id);

        if (responseEntity.getStatusCode() == HttpStatus.FOUND) {
            userRepository.deleteByUserId(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_DELETED_SUCCESSFULLY, null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
        }
    }

    public ResponseEntity<Object> login(User user) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUser_id(), user.getPassword())
            );

            if (auth.isAuthenticated()) {
                String token = jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getUser_id(), customerUserDetailsService.getUserDetail().getRole());


                Map<String, String> response = new HashMap<>();
                response.put("token", token);

                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body("Invalid Data");
        } catch (Exception ex) {
            CommonUtils.TIME_LABS_LOGGER.info("error");
        }

        return ResponseEntity.badRequest().body(Constants.ERROR);
    }
}
