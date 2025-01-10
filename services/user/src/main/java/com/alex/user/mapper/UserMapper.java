package com.alex.user.mapper;

import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import com.alex.user.model.Role;
import com.alex.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserMapper {

    public User toUser(UserRequest userRequest){
        return User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .username(userRequest.username())
                .password(userRequest.password())
                .createdAt(LocalDateTime.now())
                .role(Role.USER)
                .build();
    }

    public UserResponse toUserResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }
}
