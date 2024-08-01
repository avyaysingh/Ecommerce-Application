package com.avyay.user_service.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.avyay.user_service.dto.UserRequest;
import com.avyay.user_service.dto.UserResponse;
import com.avyay.user_service.exception.UserNotFoundException;
import com.avyay.user_service.models.Role;
import com.avyay.user_service.models.User;
import com.avyay.user_service.repositories.RoleRepository;
import com.avyay.user_service.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(this::convertUsertoUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        return convertUsertoUserResponse(user);
    }

    public UserResponse searchUser(String keywords) {
        User user = userRepository.findByUsernameOrEmail(keywords, keywords)
                .orElseThrow(() -> new UserNotFoundException("User Not Found for given keyword"));

        return convertUsertoUserResponse(user);
    }

    public void updateUserById(UserRequest userDto) {
        User user = userRepository.findByUsernameOrEmail(userDto.getUsername(), userDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        // user.setPassword(encoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());

        Role role = roleRepository.findByName(userDto.getRole())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(userDto.getRole());
                    return roleRepository.save(newRole);
                });

        user.getRoles().add(role);

        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
        userRepository.deleteById(userId);
    }

    public String findUsernameById(Long userId) {
        return userRepository.findUsernameById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"))
                .getUsername();
    }

    private UserResponse convertUsertoUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

}
