package com.avyay.e_commerce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.avyay.e_commerce.dto.User;
import com.avyay.e_commerce.feign.UserServiceClient;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<User> response = userServiceClient.getUserDetailsByUsername(username);
        User user = response.getBody();

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;

    }

}
