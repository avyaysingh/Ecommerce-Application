package com.avyay.e_commerce.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.avyay.e_commerce.dto.LoginRequest;
import com.avyay.e_commerce.dto.LoginResponse;
import com.avyay.e_commerce.dto.MessageResponse;
import com.avyay.e_commerce.dto.SignupRequest;

import jakarta.validation.Valid;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @PostMapping("/api/auth/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    @PostMapping("/api/auth/signup")
    public ResponseEntity<MessageResponse> signup(@RequestBody SignupRequest signupRequest);

    @GetMapping("/api/users/{userId}/username")
    public ResponseEntity<String> findUsernameById(@PathVariable Long userId);

}
