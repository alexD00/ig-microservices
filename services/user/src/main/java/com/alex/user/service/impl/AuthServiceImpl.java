package com.alex.user.service.impl;

import com.alex.user.dto.AuthRequest;
import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;
import com.alex.user.mapper.UserMapper;
import com.alex.user.model.User;
import com.alex.user.repository.UserRepository;
import com.alex.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(@Valid UserRequest userRequest){
        User user = userMapper.toUser(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse authenticate(@Valid AuthRequest authRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password()
                    )
            );
        }catch (AuthenticationException e){
            throw new BadCredentialsException("Invalid email / password");
        }

        User user = userRepository.findByUsername(authRequest.username()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
