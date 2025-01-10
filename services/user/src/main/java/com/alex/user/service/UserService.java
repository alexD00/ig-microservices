package com.alex.user.service;

import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

//    UserResponse createUser(@Valid UserRequest userRequest);

    UserResponse updateUser(Integer userId, @Valid UserRequest userRequest);

    UserResponse findUserById(Integer userId);

    List<UserResponse> findAllUsers();

    String deleteUserById(Integer userId);
}
