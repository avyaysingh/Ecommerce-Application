package com.avyay.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.avyay.e_commerce.dto.LoginRequest;
import com.avyay.e_commerce.dto.LoginResponse;
import com.avyay.e_commerce.dto.MessageResponse;
import com.avyay.e_commerce.dto.SignupRequest;
import com.avyay.e_commerce.feign.UserServiceClient;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private HttpSession session;

    @GetMapping("/test")
    public String test() {
        return "Hello";
    }

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ResponseEntity<LoginResponse> response = userServiceClient.authenticateUser(loginRequest);
        if (response.getStatusCode() == HttpStatus.OK) {
            // Store the JWT token in the session
            session.setAttribute("token", response.getBody().getToken());
        }
        return response;

    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody SignupRequest signupRequest) {
        return userServiceClient.signup(signupRequest);
    }

    private String obtainTokenSomehow(){
        return (String) session.getAttribute("token");
    }
}
