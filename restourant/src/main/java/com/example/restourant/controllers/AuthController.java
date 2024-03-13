package com.example.restourant.controllers;


import com.example.restourant.dtos.AuthenticationRequest;
import com.example.restourant.dtos.AuthenticationResponse;
import com.example.restourant.dtos.SignupRequest;
import com.example.restourant.dtos.UserDto;
import com.example.restourant.services.auth.AuthService;
import com.example.restourant.services.auth.jwt.UserDetailsServiceImpl;
import com.example.restourant.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private  final JwtUtil jwtUtil ;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(JwtUtil jwtUtil, AuthService authService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> singnupUser(@RequestBody SignupRequest signupRequest) {

        UserDto createdUserDto = authService.createUser(signupRequest);
        if (createdUserDto == null) {
            return new ResponseEntity<>("usernot created ", HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                                            HttpServletResponse response) throws IOException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("incorrect email or password");
        } catch (DisabledException disabledException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "user not active");
            return null;
        }
        final UserDetails userDetails =userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return new AuthenticationResponse(jwt);
        }




    }


