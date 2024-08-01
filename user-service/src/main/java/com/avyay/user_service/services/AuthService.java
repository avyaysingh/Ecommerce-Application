package com.avyay.user_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.avyay.user_service.dto.LoginRequest;
import com.avyay.user_service.dto.LoginResponse;
import com.avyay.user_service.dto.MessageResponse;
import com.avyay.user_service.dto.SignupRequest;
import com.avyay.user_service.exception.UserNotFoundException;
import com.avyay.user_service.jwt.JwtAuthenticationHelper;
import com.avyay.user_service.models.Role;
import com.avyay.user_service.models.User;
import com.avyay.user_service.repositories.RoleRepository;
import com.avyay.user_service.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager manager;

    @Autowired
    JwtAuthenticationHelper jwtHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    public LoginResponse authenticateUser(LoginRequest loginRequest) {

        String usernameOrEmail = loginRequest.getUserNameOrEmail();

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UserNotFoundException("User not Found"));

        doAuthenticate(usernameOrEmail, loginRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameOrEmail);

        String token = jwtHelper.generateToken(userDetails);

        return LoginResponse.builder()
                .username(userDetails.getUsername())
                .token(token).build();
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        try {
            manager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }

    public MessageResponse signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        User user = User.builder()
                .name(signupRequest.getName())
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .build();

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        user.getRoles().add(role);

        userRepository.save(user);

        return new MessageResponse("User Registered successfully !");
    }

}
