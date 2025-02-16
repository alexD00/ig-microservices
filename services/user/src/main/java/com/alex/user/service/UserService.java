package com.alex.user.service;

import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    AuthResponse updateUser(UserRequest userRequest, String authToken);

    UserResponse findLoggedUser(String authToken);

    UserResponse findUserById(Integer userId, String authToken);

    List<UserResponse> findAllUsers(String authToken);

    String deleteLoggedUser(String authToken);
}
