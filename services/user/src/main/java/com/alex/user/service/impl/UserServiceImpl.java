package com.alex.user.service.impl;

import com.alex.user.client.ActionClient;
import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.FollowerRequest;
import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import com.alex.user.exception.UserPermissionException;
import com.alex.user.mapper.UserMapper;
import com.alex.user.model.User;
import com.alex.user.repository.UserRepository;
import com.alex.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ActionClient actionClient;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String ACTIONTOPIC = "action-topic";
    private static final String POSTTOPIC = "post-topic";

    @Override
    public AuthResponse updateUser(UserRequest userRequest, String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        User user = userRepository.findById(authUserId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + authUserId + " was not found"));

        // If user changes their account from private to public, automatically approve all follower requests
        if (!user.getIsAccountPublic() && userRequest.isAccountPublic()){
            String message = "BATCHAPPROVE_" + user.getId();
            kafkaTemplate.send(ACTIONTOPIC, message);
        }

        user.setFirstName(userRequest.firstName());
        user.setLastName(userRequest.lastName());
        user.setUsername(userRequest.username());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.setIsAccountPublic(userRequest.isAccountPublic() == null ? user.getIsAccountPublic() : userRequest.isAccountPublic());

        userRepository.save(user);
        log.info("User with id: {} was updated successfully", user.getId());

        String token = jwtService.generateToken(user);

        // Returns token when updating user because when authorizing the user is looked up by email
        // if the email changes the user won't be found because the old token contains the old email
        // A new token will be generated so that email updates all always included
        return new AuthResponse("User was updated successfully", token);
    }

    @Override
    public UserResponse findLoggedUser(String authToken) {
        int userId = jwtService.extractUserId(authToken.substring(7));

        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));
    }

    @Override
    public UserResponse findUserById(Integer userId, String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        if (!userRepository.existsById(authUserId)){
            throw new EntityNotFoundException("User with id: " + authUserId + " was not found");
        }

        return userRepository.findById(userId)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));
    }

    @Override
    public List<UserResponse> findAllUsers(int page, int size, String direction, String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        if (!userRepository.existsById(authUserId)){
            throw new EntityNotFoundException("User with id: " + authUserId + " was not found");
        }

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"));

        return userRepository.findAll(pageRequest)
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public List<UserResponse> findAllLoggedUserFollowers(String authToken) {
        int userId = jwtService.extractUserId(authToken.substring(7));
        List<UserResponse> followers = new ArrayList<>();
        List<Integer> followersIds = actionClient.findFollowersIdOfUser(userId);

        for (int id: followersIds){
            if (userRepository.findById(id).isPresent()) {
                followers.add(userMapper.toUserResponse(userRepository.findById(id).get()));
            }
        }

        return followers;
    }

    @Override
    public List<UserResponse> findAllLoggedUserFollowings(String authToken) {
        int userId = jwtService.extractUserId(authToken.substring(7));
        List<UserResponse> followings = new ArrayList<>();
        List<Integer> followingsIds = actionClient.findFollowingsIdOfUser(userId);

        for (int id: followingsIds){
            followings.add(userMapper.toUserResponse(userRepository.findById(id).get()));
        }

        return followings;
    }

    @Override
    public List<UserResponse> findUserFollowers(Integer userId, String authToken) {
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        User user = userRepository.
                findById(userId).
                orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

        List <UserResponse> followers = new ArrayList<>();
        List <Integer> followersIds = actionClient.findFollowersIdOfUser(userId);

        if (user.getId() != authUserId) { // User can see their own followers
            if (!user.getIsAccountPublic() && !followersIds.contains(authUserId)) { // Throw exception if account is private and logged user doesn't follow the target user
                    throw new UserPermissionException("The account of userId: " + userId + " is private. You need to follow this user to see their followers");
            }
        }

        for (int id: followersIds){
            followers.add(userMapper.toUserResponse(userRepository.findById(id).get()));
        }

        return followers;
    }

    @Override
    public List<UserResponse> findUserFollowings(Integer userId, String authToken) {
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        User user = userRepository.
                findById(userId).
                orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " was not found"));

        List<UserResponse> followings = new ArrayList<>();
        List<Integer> followingsIds = actionClient.findFollowingsIdOfUser(userId);
        List<Integer> followersIds = actionClient.findFollowersIdOfUser(userId);

        if (user.getId() != authUserId){ // User can see their own followings
            if (!user.getIsAccountPublic() && !followersIds.contains(authUserId)){ // Throw exception if account is private and logged user doesn't follow the target user
                throw new UserPermissionException("The account of userId: " + userId + " is private. You need to follow this user to see their followings");
            }
        }

        for (int id: followingsIds){
            followings.add(userMapper.toUserResponse(userRepository.findById(id).get()));
        }

        return followings;
    }

    @Override
    public List<UserResponse> findFollowRequestsOfLoggedUser(String authToken) {
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        List<FollowerRequest> followerRequestList = actionClient.findFollowerRequestsOfLoggedUser(String.valueOf(authUserId));
        List<UserResponse> userRequestList = new ArrayList<>();

        for (FollowerRequest request: followerRequestList){
            userRequestList.add(userMapper.toUserResponse(userRepository.findById(request.followerRequesterId()).get()));
        }

        return userRequestList;
    }

    @Override
    public boolean findUserAccountStatus(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new EntityNotFoundException("User with id: " + userId + " was not found");
        }

        return user.get().getIsAccountPublic();
    }

    @Override
    public String deleteLoggedUser(String authToken){
        int authUserId = jwtService.extractUserId(authToken.substring(7));
        if (!userRepository.existsById(authUserId)){
            log.error("Attempted to delete non existing user with id: {}", authUserId);
            throw new EntityNotFoundException("User with id: " + authUserId + " was not found");
        }

        // Remove user activity in action service
        String message = "DELETEUSERACTIVITY_" + authUserId;
        kafkaTemplate.send(ACTIONTOPIC, message);

        // Delete all user posts
        message = "DELETEUSERPOSTS_" + authUserId;
        kafkaTemplate.send(POSTTOPIC, message);

        userRepository.deleteById(authUserId);
        log.warn("User with id: {} was deleted successfully", authUserId);

        return "Deleted user with id: " + authUserId;
    }
}
