package com.alex.user.controller;

import com.alex.user.dto.AuthResponse;
import com.alex.user.dto.UserRequest;
import com.alex.user.dto.UserResponse;
import com.alex.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<AuthResponse> updateUser(
            @RequestBody @Valid UserRequest userRequest,
            @RequestHeader(value = "Authorization") String authToken
    ){
        return ResponseEntity.ok(userService.updateUser(userRequest, authToken));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> findLoggedUser(
            @RequestHeader(value = "Authorization") String authToken
    ){
       return ResponseEntity.ok(userService.findLoggedUser(authToken));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> findUserById(
            @PathVariable("user-id") Integer userId,
            @RequestHeader(value = "Authorization") String authToken
    ){
        return ResponseEntity.ok(userService.findUserById(userId, authToken));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestHeader(value = "Authorization") String authToken
    ){
        return ResponseEntity.ok(userService.findAllUsers(page, size, direction, authToken));
    }

    @GetMapping("/me/followers")
    public ResponseEntity<List<UserResponse>> findAllLoggedUserFollowers(
            @RequestHeader(value = "Authorization") String authToken
    ){
        return ResponseEntity.ok(userService.findAllLoggedUserFollowers(authToken));
    }

    @GetMapping("/me/followings")
    public ResponseEntity<List<UserResponse>> findAllLoggedUserFollowings(
            @RequestHeader(value = "Authorization") String authToken
    ){
        return ResponseEntity.ok(userService.findAllLoggedUserFollowings(authToken));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteLoggedUser(
            @RequestHeader(value = "Authorization") String authToken
    ){
        return ResponseEntity.ok(userService.deleteLoggedUser(authToken));
    }
}
