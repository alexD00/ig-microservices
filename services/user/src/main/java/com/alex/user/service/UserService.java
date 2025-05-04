package com.alex.user.service;

import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    AuthResponse updateUser(UserRequest userRequest, String authToken);

    UserResponse findLoggedUser(String authToken);

    UserResponse findUserById(Integer userId, String authToken);

    List<UserResponse> findAllUsers(int page, int size, String direction, String authToken);

    List<UserResponse> findAllLoggedUserFollowers(String authToken);

    List<UserResponse> findAllLoggedUserFollowings(String authToken);

    List<UserResponse> findUserFollowers(Integer userId, String authToken);

    List<UserResponse> findUserFollowings(Integer userId, String authToken);

    List<UserResponse> findFollowRequestsOfLoggedUser(String authToken);

    String deleteLoggedUser(String authToken);
}
