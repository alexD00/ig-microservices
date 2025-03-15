package com.alex.user.service.impl;

import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import com.alex.user.mapper.UserMapper;
import com.alex.user.model.User;
import com.alex.user.repository.UserRepository;
import com.alex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse updateUser(UserRequest userRequest, String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        User user = userRepository.findById(authUserId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + authUserId + " was not found"));
        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userRepository.save(user);
        log.info("User with id: {} was updated successfully", user.getId());

        String token = jwtService.generateToken(user);

        // Returns token when updating user because when authorizing the user is looked up by email
        // if the email changes the user won't be found because the old token contains the old email
        // A new token will be generated so that email updates all always included
        return new AuthResponse("User was updated successfully", token);
    }

    @Override
    public UserResponse findLoggedUser(String authToken) {
        int userId = jwtService.extractUserId(authToken.substring(7));

        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));
    }

    @Override
    public UserResponse findUserById(Integer userId, String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        if (!userRepository.existsById(authUserId)){
            throw new EntityNotFoundException("User with id: " + authUserId + " was not found");
        }

        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));
    }

    @Override
    public List<UserResponse> findAllUsers(int page, int size, String direction, String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        if (!userRepository.existsById(authUserId)){
            throw new EntityNotFoundException("User with id: " + authUserId + " was not found");
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"));

        return userRepository.findAll(pageRequest)
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public String deleteLoggedUser(String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        if (!userRepository.existsById(authUserId)){
            log.error("Attempted to delete non existing user with id: {}", authUserId);
            throw new EntityNotFoundException("User with id: " + authUserId + " was not found");
        }
        userRepository.deleteById(authUserId);
        log.warn("User with id: {} was deleted successfully", authUserId);

        return "Deleted user with id: " + authUserId;
    }
}
