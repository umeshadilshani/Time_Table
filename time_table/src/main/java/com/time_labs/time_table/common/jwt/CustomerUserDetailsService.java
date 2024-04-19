package com.time_labs.time_table.common.jwt;

import com.time_labs.time_table.repository.UserRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepo;

    @Getter
    com.time_labs.time_table.repository.dao.User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userRepo.findByUserId(username);

        if (!Objects.isNull(userDetail)) {
            return new User(userDetail.getUser_id(), userDetail.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
    }
}
