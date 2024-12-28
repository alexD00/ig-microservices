package com.alex.user.mapper;

import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import com.alex.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserMapper {

    public User toUser(UserRequest userRequest){
        return User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .email(userRequest.email())
                .password(userRequest.password())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponse toUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
