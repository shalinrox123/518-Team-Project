package com.teamproject.userauth.service;

import com.teamproject.userauth.model.User;
import com.teamproject.userauth.web.dto.UserRegistrationDto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

    User findByUsername(String username);
    User save(UserRegistrationDto registrationDto);
}
