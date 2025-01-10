package com.alex.user.service.impl;

import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import com.alex.user.mapper.UserMapper;
import com.alex.user.model.User;
import com.alex.user.repository.UserRepository;
import com.alex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

//    public UserResponse createUser(@Valid UserRequest userRequest) {
//        User user = userMapper.toUser(userRequest);
//
//        return userMapper.toUserResponse(userRepository.save(user));
//    }

    public UserResponse updateUser(Integer userId, @Valid UserRequest userRequest){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse findUserById(Integer userId){
        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));
    }

    public List<UserResponse> findAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @CacheEvict(value = "users", key = "#userId")
    public String deleteUserById(Integer userId){
        if (!userRepository.existsById(userId)){
            throw new EntityNotFoundException("User with id: " + userId + " was not found");
        }
        userRepository.deleteById(userId);

        return "Deleted user with id: " + userId;
    }
}
