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
                .numFollowers(0)
                .numFollowings(0)
                // Account is public by default
                .isAccountPublic(userRequest.isAccountPublic() == null ? true : userRequest.isAccountPublic())
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
                user.getNumFollowers(),
                user.getNumFollowings(),
                user.getIsAccountPublic(),
                user.getCreatedAt()
        );
    }
}
