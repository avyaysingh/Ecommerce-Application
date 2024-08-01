package com.avyay.user_service.security;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.avyay.user_service.dto.UserDetailsResponse;
import com.avyay.user_service.models.User;
import com.avyay.user_service.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    // public UserDetailsResponse getUserDetailsByUsername(String username) throws
    // UsernameNotFoundException {
    // User user = userRepository.findByUsername(username)
    // .or(() -> userRepository.findByEmail(username))
    // .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    // return new UserDetailsResponse(user.getUsername(), user.getEmail(),
    // user.getRoles().toString()); // Modify to match your roles structure
    // }

    public UserDetailsResponse getUserDetailsByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        return new UserDetailsResponse(user.getUsername(), user.getEmail(), user.getRoles().stream()
                .map(role -> role.getName()) // Assuming role.getName() returns a string
                .collect(Collectors.joining(",")));
    }

}
