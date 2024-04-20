package com.time_labs.time_table.service;

import com.time_labs.time_table.common.CommonUtils;
import com.time_labs.time_table.common.Constants;
import com.time_labs.time_table.common.jwt.CustomerUserDetailsService;
import com.time_labs.time_table.common.jwt.JwtUtil;
import com.time_labs.time_table.common.mapper.DtoMapper;
import com.time_labs.time_table.common.validation.UserValidation;
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
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> createUser(User user) {
        try {
            // Check if the user already exists
            if (getUserById(user.getUser_id()).getStatusCode() == HttpStatus.FOUND) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_ALREADY_EXISTS, null));
            }

            // Validate the user data
            if (UserValidation.validateUser(user).getStatusCode() == HttpStatus.OK) {
                User newUser = new User();
                newUser.setUser_id(user.getUser_id());
                newUser.setName(user.getName());
                // Bcrypt the password
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                newUser.setPassword(encodedPassword);
                newUser.setRole(user.getRole());
                if (user.getGroupId() != null) {
                    newUser.setGroupId(user.getGroupId());
                }
                User savedUser = userRepository.save(newUser);
                return ResponseEntity.status(HttpStatus.CREATED).body(createResponse(Constants.MESSAGE_SUCCESSFUL, DtoMapper.mapToUserDto(savedUser)));
            }
            return UserValidation.validateUser(user);
        } catch (Exception e) {
            // Log the error
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error creating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getAllUser() {
        try {
            List<UserDto> users = userRepository.findAll()
                    .stream()
                    .map(DtoMapper::mapToUserDto)
                    .toList();

            if (users.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_NO_RECORDS_FOUND, null));
            }

            return ResponseEntity.status(HttpStatus.FOUND).body(createResponse(Constants.MESSAGE_FOUND, users));
        } catch (Exception e) {
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error getting all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> getUserById(String user_id) {
        try {
            User user = userRepository.findByUserId(user_id);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.FOUND).body(createResponse(Constants.MESSAGE_FOUND, DtoMapper.mapToUserDto(user)));
            }
            return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
        } catch (Exception e) {
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error getting user by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> updateUserById(String user_id, User user) {
        try {
            User foundUser = userRepository.findByUserId(user_id);
            if (foundUser != null) {
                User updateUser = foundUser;
                updateUser.setName(user.getName());
                // Bcrypt the password
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                updateUser.setPassword(encodedPassword);
                updateUser.setRole(user.getRole());

                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_UPDATED_SUCCESSFULLY, DtoMapper.mapToUserDto(userRepository.save(updateUser))));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
        } catch (Exception e) {
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error updating user by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }

    public ResponseEntity<Object> deleteUserById(String user_id) {
        try {
            ResponseEntity<Object> responseEntity = getUserById(user_id);
            if (responseEntity.getStatusCode() == HttpStatus.FOUND) {
                userRepository.deleteByUserId(user_id);
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_DELETED_SUCCESSFULLY, null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
            }
        } catch (Exception e) {
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error deleting user by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
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
        } catch (Exception e) {
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error during login", e);
            return ResponseEntity.badRequest().body(Constants.ERROR);
        }
    }

    public ResponseEntity<Object> resetPassword(String user_id, User user) {
        try {
            User foundUser = userRepository.findByUserId(user_id);
            if (foundUser != null) {
                User updateUser = foundUser;
                // Bcrypt the password
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                updateUser.setPassword(encodedPassword);
                return ResponseEntity.status(HttpStatus.OK).body(createResponse(Constants.MESSAGE_UPDATED_SUCCESSFULLY, DtoMapper.mapToUserDto(userRepository.save(updateUser))));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(createResponse(Constants.MESSAGE_NOT_FOUND, null));
        } catch (Exception e) {
            CommonUtils.TIME_LABS_LOGGER.log(Level.SEVERE, "Error resetting password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createResponse(e.getMessage(), null));
        }
    }
}
