package com.time_labs.time_table.service;

import com.time_labs.time_table.repository.UserRepository;
import com.time_labs.time_table.repository.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    UserRepository userRepository;

    public String sendChangeAlert(String groupId, List<User> lecturers) {
        List<User> users = userRepository.findByGroupId(groupId);

        List<String> studentNames = users.stream().map(User::getName).collect(Collectors.toList());

        List<String> lecturerNames = lecturers.stream()
                .map(User::getName)
                .collect(Collectors.toList());

        String message = "Changes have been made for group " + groupId + ". Notification have been sent to the : "
                + String.join(", ", lecturerNames) + ", " + String.join(", ", studentNames);

        return message;
    }
}
