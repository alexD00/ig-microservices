package com.alex.user.service;

import com.alex.user.dto.AuthRequest;
import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;
import jakarta.validation.Valid;

public interface AuthService {

    AuthResponse register(@Valid UserRequest userRequest);

    AuthResponse authenticate(@Valid AuthRequest authRequest);
}
