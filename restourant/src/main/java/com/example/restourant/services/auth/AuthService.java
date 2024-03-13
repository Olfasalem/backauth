package com.example.restourant.services.auth;

import com.example.restourant.dtos.SignupRequest;
import com.example.restourant.dtos.UserDto;

public interface AuthService {
    UserDto createUser(SignupRequest signupRequest);
}
