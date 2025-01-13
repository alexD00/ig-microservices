package com.alex.user.controller;

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

    @PutMapping("/{user-id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("user-id") Integer userId,
            @RequestBody @Valid UserRequest userRequest
    ){
        return ResponseEntity.ok(userService.updateUser(userId, userRequest));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserResponse> findUserById(
            @PathVariable("user-id") Integer userId
    ){
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers(){
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<String> deleteUserById(
            @PathVariable("user-id") Integer userId
    ){
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }
}
