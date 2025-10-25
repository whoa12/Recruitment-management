package com.projects.RecruiterManagement.controller;


import com.projects.RecruiterManagement.config.JwtGenerator;
import com.projects.RecruiterManagement.model.User_Role;
import com.projects.RecruiterManagement.repository.UserRepository;
import com.projects.RecruiterManagement.request.LoginRequest;
import com.projects.RecruiterManagement.response.AuthResponse;
import com.projects.RecruiterManagement.service.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.projects.RecruiterManagement.model.User;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;

    private final CustomerUserDetailsService customerUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) {
        if(userRepository.findByEmail(user.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, "User already exists with the given E-mail!", null));
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User createdUser = new User();
        createdUser.setName(user.getName());
        createdUser.setEmail(user.getEmail());
        createdUser.setAddress(user.getAddress());
        createdUser.setPassword(encodedPassword);
        createdUser.setRole(user.getRole());
        createdUser.setProfileHeadline(user.getProfileHeadline());

        User savedUser = userRepository.save(createdUser);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtGenerator.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Registration successful!");
        authResponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        String username = request.getEmail();
        String password = request.getPassword();
        Authentication authentication = authenticate(username, password);
        String jwt = jwtGenerator.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login successful!");
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
        authResponse.setRole(User_Role.valueOf(role));
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {

            throw new BadCredentialsException("Invalid username!");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }


}
