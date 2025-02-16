package com.alex.user.service;

import com.alex.user.dto.AuthRequest;
import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;

public interface AuthService {

    String register(UserRequest userRequest);

    AuthResponse authenticate(AuthRequest authRequest);
}
